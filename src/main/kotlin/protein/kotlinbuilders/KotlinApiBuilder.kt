package protein.kotlinbuilders

import com.google.gson.annotations.SerializedName
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asClassName
import io.reactivex.Completable
import io.reactivex.Single
import io.swagger.models.HttpMethod
import io.swagger.models.ModelImpl
import io.swagger.models.Operation
import io.swagger.models.RefModel
import io.swagger.models.ArrayModel
import io.swagger.models.Swagger
import io.swagger.models.parameters.BodyParameter
import io.swagger.models.parameters.Parameter
import io.swagger.models.parameters.PathParameter
import io.swagger.models.parameters.QueryParameter
import io.swagger.models.properties.ArrayProperty
import io.swagger.models.properties.DoubleProperty
import io.swagger.models.properties.FloatProperty
import io.swagger.models.properties.IntegerProperty
import io.swagger.models.properties.LongProperty
import io.swagger.models.properties.Property
import io.swagger.models.properties.RefProperty
import io.swagger.models.properties.StringProperty
import io.swagger.parser.SwaggerParser
import protein.common.StorageUtils
import protein.tracking.ErrorTracking
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.FileNotFoundException
import java.lang.IllegalStateException
import java.net.UnknownHostException
import java.util.ArrayList

class KotlinApiBuilder(
  private val proteinApiConfiguration: ProteinApiConfiguration,
  private val errorTracking: ErrorTracking
) {
  companion object {
    const val OK_RESPONSE = "200"
    const val ARRAY_SWAGGER_TYPE = "array"
    const val INTEGER_SWAGGER_TYPE = "integer"
    const val NUMBER_SWAGGER_TYPE = "number"
    const val STRING_SWAGGER_TYPE = "string"
    const val BOOLEAN_SWAGGER_TYPE = "boolean"
    const val REF_SWAGGER_TYPE = "ref"
  }

  private val swaggerModel: Swagger = try {
    if (!proteinApiConfiguration.swaggerUrl.isEmpty()) {
      SwaggerParser().read(proteinApiConfiguration.swaggerUrl)
    } else {
      SwaggerParser().read(proteinApiConfiguration.swaggerFile)
    }
  } catch (unknown: UnknownHostException) {
    errorTracking.logException(unknown)
    Swagger()
  } catch (illegal: IllegalStateException) {
    errorTracking.logException(illegal)
    Swagger()
  } catch (notFound: FileNotFoundException) {
    errorTracking.logException(notFound)
    Swagger()
  }

  private lateinit var apiInterfaceTypeSpec: TypeSpec
  private val responseBodyModelListTypeSpec: ArrayList<TypeSpec> = ArrayList()
  private val enumListTypeSpec: ArrayList<TypeSpec> = ArrayList()

  fun build() {
    createEnumClasses()
    apiInterfaceTypeSpec = createApiRetrofitInterface(createApiResponseBodyModel())
  }

  fun getGeneratedTypeSpec(): TypeSpec {
    return apiInterfaceTypeSpec
  }

  fun generateFiles() {
    StorageUtils.generateFiles(
      proteinApiConfiguration.moduleName, proteinApiConfiguration.packageName, apiInterfaceTypeSpec)

    for (typeSpec in responseBodyModelListTypeSpec) {
      StorageUtils.generateFiles(
        proteinApiConfiguration.moduleName, proteinApiConfiguration.packageName, typeSpec)
    }

    for (typeSpec in enumListTypeSpec) {
      StorageUtils.generateFiles(
        proteinApiConfiguration.moduleName, proteinApiConfiguration.packageName, typeSpec)
    }
  }

  private fun createEnumClasses() {
    addOperationResponseEnums()
    addModelEnums()
  }

  private fun addModelEnums() {
    if (swaggerModel.definitions != null && !swaggerModel.definitions.isEmpty()) {
      for (definition in swaggerModel.definitions) {
        if (definition.value != null && definition.value.properties != null) {
          for (modelProperty in definition.value.properties) {
            if (modelProperty.value is StringProperty) {
              val enumDefinition = (modelProperty.value as StringProperty).enum
              if (enumDefinition != null) {
                val enumTypeSpecBuilder = TypeSpec.enumBuilder(modelProperty.key.capitalize())
                for (constant in enumDefinition) {
                  enumTypeSpecBuilder.addEnumConstant(constant)
                }
                if (!enumListTypeSpec.contains(enumTypeSpecBuilder.build())) {
                  enumListTypeSpec.add(enumTypeSpecBuilder.build())
                }
              }
            }
          }
        }
      }
    }
  }

  private fun addOperationResponseEnums() {
    if (swaggerModel.paths != null && !swaggerModel.paths.isEmpty()) {
      for (path in swaggerModel.paths) {
        for (operation in path.value.operationMap) {
          try {
            for (parameters in operation.value.parameters) {
              if (parameters is PathParameter) {
                if (parameters.enum != null) {
                  val enumTypeSpecBuilder = TypeSpec.enumBuilder(parameters.name.capitalize())
                  for (constant in parameters.enum) {
                    enumTypeSpecBuilder.addEnumConstant(constant)
                  }
                  if (!enumListTypeSpec.contains(enumTypeSpecBuilder.build())) {
                    enumListTypeSpec.add(enumTypeSpecBuilder.build())
                  }
                }
              }
            }
          } catch (error: Exception) {
            errorTracking.logException(error)
          }
        }
      }
    }
  }

  private fun createApiResponseBodyModel(): List<String> {
    val classNameList = ArrayList<String>()

    if (swaggerModel.definitions != null && !swaggerModel.definitions.isEmpty()) {
      for (definition in swaggerModel.definitions) {

        var modelClassTypeSpec: TypeSpec.Builder
        try {
          modelClassTypeSpec = TypeSpec.classBuilder(definition.key).addModifiers(KModifier.DATA)
          classNameList.add(definition.key)
        } catch (error: IllegalArgumentException) {
          modelClassTypeSpec = TypeSpec.classBuilder("Model" + definition.key.capitalize()).addModifiers(KModifier.DATA)
          classNameList.add("Model" + definition.key.capitalize())
        }

        if (definition.value != null && definition.value.properties != null) {
          val primaryConstructor = FunSpec.constructorBuilder()
          for (modelProperty in definition.value.properties) {
            val typeName: TypeName = getTypeName(modelProperty)
            val propertySpec = PropertySpec.builder(modelProperty.key, typeName)
              .addAnnotation(AnnotationSpec.builder(SerializedName::class)
                .addMember("\"${modelProperty.key}\"")
                .build())
              .initializer(modelProperty.key)
              .build()
            primaryConstructor.addParameter(modelProperty.key, typeName)
            modelClassTypeSpec.addProperty(propertySpec)
          }
          modelClassTypeSpec.primaryConstructor(primaryConstructor.build())

          responseBodyModelListTypeSpec.add(modelClassTypeSpec.build())
        }
      }
    }

    return classNameList
  }

  private fun createApiRetrofitInterface(classNameList: List<String>): TypeSpec {
    val apiInterfaceTypeSpecBuilder = TypeSpec
      .interfaceBuilder("${proteinApiConfiguration.componentName}ApiInterface")
      .addModifiers(KModifier.PUBLIC)

    addApiPathMethods(apiInterfaceTypeSpecBuilder, classNameList)

    return apiInterfaceTypeSpecBuilder.build()
  }

  private fun addApiPathMethods(apiInterfaceTypeSpec: TypeSpec.Builder, classNameList: List<String>) {
    if (swaggerModel.paths != null && !swaggerModel.paths.isEmpty()) {
      for (path in swaggerModel.paths) {
        for (operation in path.value.operationMap) {

          val annotationSpec: AnnotationSpec = when {
            operation.key.name.contains(
              "GET") -> AnnotationSpec.builder(GET::class).addMember("\"${path.key}\"").build()
            operation.key.name.contains(
              "POST") -> AnnotationSpec.builder(POST::class).addMember("\"${path.key}\"").build()
            operation.key.name.contains(
              "PUT") -> AnnotationSpec.builder(PUT::class).addMember("\"${path.key}\"").build()
            operation.key.name.contains(
              "PATCH") -> AnnotationSpec.builder(PATCH::class).addMember("\"${path.key}\"").build()
            operation.key.name.contains(
              "DELETE") -> AnnotationSpec.builder(DELETE::class).addMember("\"${path.key}\"").build()
            else -> AnnotationSpec.builder(GET::class).addMember("\"${path.key}\"").build()
          }

          try {
            val doc = ((listOf(operation.value.summary + "\n") + getMethodParametersDocs(operation)).joinToString("\n")).trim()

            val returnedClass = getReturnedClass(operation, classNameList)
            val methodParameters = getMethodParameters(operation)
            val funSpec = FunSpec.builder(operation.value.operationId)
              .addModifiers(KModifier.PUBLIC, KModifier.ABSTRACT)
              .addAnnotation(annotationSpec)
              .addParameters(methodParameters)
              .returns(returnedClass)
              .addKdoc("$doc\n")
              .build()

            apiInterfaceTypeSpec.addFunction(funSpec)
          } catch (exception: Exception) {
            errorTracking.logException(exception)
          }
        }
      }
    }
  }

  private fun getMethodParametersDocs(operation: MutableMap.MutableEntry<HttpMethod, Operation>): Iterable<String> {
    return operation.value.parameters.filterNot { it.description.isNullOrBlank() }.map { "@param ${it.name} ${it.description}" }
  }

  private fun getTypeName(modelProperty: MutableMap.MutableEntry<String, Property>): TypeName {
    val property = modelProperty.value
    return when {
      property.type == REF_SWAGGER_TYPE ->
        TypeVariableName.invoke((property as RefProperty).simpleRef).requiredOrNullable(property.required)

      property.type == ARRAY_SWAGGER_TYPE -> {
        val arrayProperty = property as ArrayProperty
        getTypedArray(arrayProperty.items).requiredOrNullable(arrayProperty.required)
      }
      else -> getKotlinClassTypeName(property.type, property.format).requiredOrNullable(property.required)
    }
  }

  private fun getMethodParameters(
    operation: MutableMap.MutableEntry<HttpMethod, Operation>
  ): Iterable<ParameterSpec> {
    return operation.value.parameters.mapNotNull { parameter ->
      // Transform parameters in the format foo.bar to fooBar
      val name = parameter.name.split('.').mapIndexed { index, s -> if (index > 0) s.capitalize() else s }.joinToString("")
      when (parameter.`in`) {
        "body" -> {
          ParameterSpec.builder(name, getBodyParameterSpec(parameter))
            .addAnnotation(AnnotationSpec.builder(Body::class).build()).build()
        }
        "path" -> {
          val type = getKotlinClassTypeName((parameter as PathParameter).type, parameter.format).requiredOrNullable(parameter.required)
          ParameterSpec.builder(name, type)
            .addAnnotation(AnnotationSpec.builder(Path::class).addMember("\"${parameter.name}\"").build()).build()
        }
        "query" -> {
          if ((parameter as QueryParameter).type == ARRAY_SWAGGER_TYPE) {
            val type = List::class.asClassName().parameterizedBy(getKotlinClassTypeName(parameter.items.type)).requiredOrNullable(parameter.required)
            ParameterSpec.builder(name, type)
          } else {
            val type = getKotlinClassTypeName(parameter.type, parameter.format).requiredOrNullable(parameter.required)
            ParameterSpec.builder(name, type)
          }.addAnnotation(AnnotationSpec.builder(Query::class).addMember("\"${parameter.name}\"").build()).build()
        }
        else -> null
      }
    }
  }

  private fun getBodyParameterSpec(parameter: Parameter): TypeName {
    val bodyParameter = parameter as BodyParameter
    val schema = bodyParameter.schema

    return when (schema) {
      is RefModel -> ClassName.bestGuess(schema.simpleRef.capitalize()).requiredOrNullable(parameter.required)

      is ArrayModel -> getTypedArray(schema.items).requiredOrNullable(parameter.required)

      else -> {
        val bodyParameter1 = parameter.schema as? ModelImpl ?: ModelImpl()

        if (STRING_SWAGGER_TYPE == bodyParameter1.type) {
          String::class.asClassName().requiredOrNullable(parameter.required)
        } else {
          ClassName.bestGuess(parameter.name.capitalize()).requiredOrNullable(parameter.required)
        }
      }
    }
  }

  private fun getTypedArray(items: Property): TypeName {
    val typeProperty = when (items) {
      is LongProperty -> TypeVariableName.invoke(Long::class.simpleName!!)
      is IntegerProperty -> TypeVariableName.invoke(Int::class.simpleName!!)
      is FloatProperty -> TypeVariableName.invoke(Float::class.simpleName!!)
      is DoubleProperty -> TypeVariableName.invoke(Double::class.simpleName!!)
      is RefProperty -> TypeVariableName.invoke(items.simpleRef)
      else -> getKotlinClassTypeName(items.type, items.format)
    }
    return List::class.asClassName().parameterizedBy(typeProperty)
  }

  private fun TypeName.requiredOrNullable(required: Boolean) = if (required) this else asNullable()

  private fun getReturnedClass(
    operation: MutableMap.MutableEntry<HttpMethod, Operation>,
    classNameList: List<String>
  ): TypeName {
    try {
      if (operation.value.responses[OK_RESPONSE]?.schema != null &&
        operation.value.responses[OK_RESPONSE]?.schema is RefProperty) {
        val refProperty = (operation.value.responses[OK_RESPONSE]?.schema as RefProperty)
        var responseClassName = refProperty.simpleRef
        responseClassName = getValidClassName(responseClassName, refProperty)

        if (classNameList.contains(responseClassName)) {
          return Single::class.asClassName().parameterizedBy(TypeVariableName.invoke(responseClassName))
        }
      } else if (operation.value.responses[OK_RESPONSE]?.schema != null &&
        operation.value.responses[OK_RESPONSE]?.schema is ArrayProperty) {
        val refProperty = (operation.value.responses[OK_RESPONSE]?.schema as ArrayProperty)
        var responseClassName = (refProperty.items as RefProperty).simpleRef
        responseClassName = getValidClassName(responseClassName, (refProperty.items as RefProperty))

        if (classNameList.contains(responseClassName)) {
          return Single::class.asClassName().parameterizedBy(
            List::class.asClassName().parameterizedBy(TypeVariableName.invoke(responseClassName))
          )
        }
      }
    } catch (error: ClassCastException) {
      errorTracking.logException(error)
    }

    return Completable::class.asClassName()
  }

  private fun getValidClassName(responseClassName: String, refProperty: RefProperty): String {
    var className = responseClassName
    try {
      TypeSpec.classBuilder(className)
    } catch (error: IllegalArgumentException) {
      if (refProperty.simpleRef != null) {
        className = "Model" + refProperty.simpleRef.capitalize()
      }
    }
    return className
  }

  private fun getKotlinClassTypeName(type: String, format: String? = null): TypeName {
    return when (type) {
      ARRAY_SWAGGER_TYPE -> TypeVariableName.invoke(List::class.simpleName!!)
      STRING_SWAGGER_TYPE -> TypeVariableName.invoke(String::class.simpleName!!)
      NUMBER_SWAGGER_TYPE -> TypeVariableName.invoke(Double::class.simpleName!!)
      INTEGER_SWAGGER_TYPE -> {
        when (format) {
          "int64" -> TypeVariableName.invoke(Long::class.simpleName!!)
          else -> TypeVariableName.invoke(Int::class.simpleName!!)
        }
      }
      else -> TypeVariableName.invoke(type.capitalize())
    }
  }

  /*private fun getPropertyInitializer(type: String): String {
      return when (type) {
          ARRAY_SWAGGER_TYPE -> "ArrayList()"
          INTEGER_SWAGGER_TYPE -> "0"
          STRING_SWAGGER_TYPE -> "\"\""
          BOOLEAN_SWAGGER_TYPE -> "false"
          else -> "null"
      }
  }*/

  fun getGeneratedApiInterfaceString(): String {
    return StorageUtils.generateString(proteinApiConfiguration.packageName, apiInterfaceTypeSpec)
  }

  fun getGeneratedModelsString(): String {
    var generated = ""
    for (typeSpec in responseBodyModelListTypeSpec) {
      generated += StorageUtils.generateString(proteinApiConfiguration.packageName, typeSpec)
    }
    return generated
  }

  fun getGeneratedEnums(): String {
    var generated = ""
    for (typeSpec in enumListTypeSpec) {
      generated += StorageUtils.generateString(proteinApiConfiguration.packageName, typeSpec)
    }
    return generated
  }
}
