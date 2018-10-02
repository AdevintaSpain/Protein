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
import io.swagger.models.Swagger
import io.swagger.models.parameters.BodyParameter
import io.swagger.models.parameters.Parameter
import io.swagger.models.parameters.PathParameter
import io.swagger.models.properties.ArrayProperty
import io.swagger.models.properties.Property
import io.swagger.models.properties.RefProperty
import io.swagger.models.properties.StringProperty
import io.swagger.parser.SwaggerParser
import protein.common.StorageUtils
import protein.tracking.ErrorTracking
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.DELETE
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
        classNameList.add(definition.key)

        val modelClassTypeSpec = TypeSpec.classBuilder(definition.key).addModifiers(KModifier.DATA)

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
            val returnedClass = getReturnedClass(operation, classNameList)
            val methodParameters = getMethodParameters(operation)
            val funSpec = FunSpec.builder(operation.value.operationId)
              .addModifiers(KModifier.PUBLIC, KModifier.ABSTRACT)
              .addAnnotation(annotationSpec)
              .addParameters(methodParameters)
              .returns(returnedClass)
              .build()

            apiInterfaceTypeSpec.addFunction(funSpec)
          } catch (exception: Exception) {
            errorTracking.logException(exception)
          }
        }
      }
    }
  }

  private fun getTypeName(modelProperty: MutableMap.MutableEntry<String, Property>): TypeName {
    return when {
      modelProperty.value.type == REF_SWAGGER_TYPE ->
        TypeVariableName.invoke((modelProperty.value as RefProperty).simpleRef)
      modelProperty.value.type == ARRAY_SWAGGER_TYPE -> {
        val typeProperty = try {
          ((modelProperty.value as ArrayProperty).items as RefProperty).simpleRef
        } catch (e: ClassCastException) {
          ((modelProperty.value as ArrayProperty).items.type)
        }
        return List::class.asClassName().parameterizedBy(getKotlinClassTypeName(typeProperty))
      }
      else -> getKotlinClassTypeName(modelProperty.value.type)
    }
  }

  private fun getMethodParameters(
    operation: MutableMap.MutableEntry<HttpMethod, Operation>
  ): Iterable<ParameterSpec> {
    val methodParameters = ArrayList<ParameterSpec>()
    for (parameter in operation.value.parameters) {
      if (parameter.`in` == "body") {
        val bodyParameterSpec: ParameterSpec = getBodyParameterSpec(parameter)
        methodParameters.add(bodyParameterSpec)
      } else if (parameter.`in` == "path") {
        val pathParameterSpec =
          ParameterSpec.builder(parameter.name, getKotlinClassTypeName((parameter as PathParameter).type))
            .addAnnotation(
              AnnotationSpec.builder(Path::class).addMember("\"${parameter.name}\"").build()).build()
        methodParameters.add(pathParameterSpec)
      }
    }
    return methodParameters
  }

  private fun getBodyParameterSpec(parameter: Parameter): ParameterSpec {
    val bodyParameterSpec: ParameterSpec
    val bodyParameter = try {
      (parameter as BodyParameter).schema as ModelImpl
    } catch (e: ClassCastException) {
      ModelImpl()
    }
    bodyParameterSpec = if (bodyParameter.type == STRING_SWAGGER_TYPE) {
      ParameterSpec.builder(parameter.name, String::class)
        .addAnnotation(AnnotationSpec.builder(Body::class).build()).build()
    } else {
      ParameterSpec.builder(parameter.name, ClassName.bestGuess(parameter.name.capitalize()))
        .addAnnotation(AnnotationSpec.builder(Body::class).build()).build()
    }
    return bodyParameterSpec
  }

  private fun getReturnedClass(
    operation: MutableMap.MutableEntry<HttpMethod, Operation>,
    classNameList: List<String>
  ): TypeName {
    try {
      if (operation.value.responses[OK_RESPONSE]?.schema != null &&
        operation.value.responses[OK_RESPONSE]?.schema is RefProperty) {
        val refProperty = (operation.value.responses[OK_RESPONSE]?.schema as RefProperty)
        val responseClassName = refProperty.simpleRef

        if (responseClassName != null && classNameList.contains(responseClassName)) {
          return Single::class.asClassName().parameterizedBy(TypeVariableName.invoke(responseClassName))
        }
      } else if (operation.value.responses[OK_RESPONSE]?.schema != null &&
        operation.value.responses[OK_RESPONSE]?.schema is ArrayProperty) {
        val refProperty = (operation.value.responses[OK_RESPONSE]?.schema as ArrayProperty)
        val responseClassName = (refProperty.items as RefProperty).simpleRef
        if (responseClassName != null && classNameList.contains(responseClassName)) {
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

  private fun getKotlinClassTypeName(type: String): TypeName {
    return when (type) {
      ARRAY_SWAGGER_TYPE -> TypeVariableName.invoke(List::class.simpleName!!)
      INTEGER_SWAGGER_TYPE -> TypeVariableName.invoke(Int::class.simpleName!!)
      STRING_SWAGGER_TYPE -> TypeVariableName.invoke(String::class.simpleName!!)
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
