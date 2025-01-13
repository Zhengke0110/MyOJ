package fun.timu.doj.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring 上下文获取工具
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 根据bean名称获取对应的bean实例
     * 此方法用于从Spring应用程序上下文中获取指定名称的bean，便于在应用程序中获取Spring管理的bean实例
     *
     * @param beanName bean的名称，用于标识所需的bean实例
     * @return Object 返回指定名称的bean实例，返回类型为Object，可以根据需要进行类型转换
     */
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    /**
     * 从Spring应用上下文中获取指定类型的Bean
     * 此方法用于简化从applicationContext中获取Bean的过程
     * 它通过类型参数T来确保类型安全，避免了类型转换的需要
     *
     * @param beanClass 所需Bean的类类型
     * @return 返回指定类型的Bean实例
     */
    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    /**
     * 根据bean名称和类型从应用上下文中获取指定的bean实例
     * 此方法允许通过名称和预期类型安全地检索bean，确保类型转换的正确性
     *
     * @param beanName  bean的名称，用于在应用上下文中唯一标识一个bean
     * @param beanClass bean的类型，用于指定返回值的类型
     * @return 返回指定类型的bean实例
     */
    public static <T> T getBean(String beanName, Class<T> beanClass) {
        return applicationContext.getBean(beanName, beanClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }
}
