package blog.boot.utils

import org.springframework.beans.BeanUtils
import org.springframework.beans.BeanWrapper
import org.springframework.beans.BeanWrapperImpl
import org.springframework.context.ApplicationContext
import blog.boot.support.ApplicationContextProvider
import blog.boot.support.annotation.Slf4j
import blog.boot.support.annotation.Slf4j.Companion.log
import java.beans.FeatureDescriptor
import java.util.stream.Stream

@Slf4j
object PropertyUtils {
    fun copyNonNullProperties(source: Any?, target: Any?) {
        BeanUtils.copyProperties(source!!, target!!, *getNullPropertyNames(source))
    }

    private fun getNullPropertyNames(source: Any): Array<String> {
        val wrappedSource: BeanWrapper = BeanWrapperImpl(source)
        return Stream.of(*wrappedSource.propertyDescriptors)
            .map(FeatureDescriptor::getName)
            .filter{ propertyName: String -> wrappedSource.getPropertyValue(propertyName) == null }
            .toArray{ arrayOfNulls<String>(it) }
    }

    fun copyWithoutProperties(source: Any, target: Any, properties: Array<String?>) {
        BeanUtils.copyProperties(source, target, *properties)
    }

    fun getProperty(propertyName: String): String? {
        return getProperty(propertyName, null)
    }

    fun getProperty(propertyName: String, defaultValue: String?): String? {
        var value = defaultValue
        val applicationContext: ApplicationContext? = ApplicationContextProvider.applicationContext
        if (applicationContext!!.environment.getProperty(propertyName) == null) {
            log.warn("$propertyName properties was not loaded.")
        } else {
            value = applicationContext.environment.getProperty(propertyName).toString()
        }
        return value
    }
}