package io.github.shengchaojie.spring.extend.strategy;

import com.google.common.collect.Lists;
import io.github.shengchaojie.spring.extend.strategy.exceptions.StrategyDuplicateException;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;

/**
 * @author shengchaojie
 * @date 2019-07-30
 **/
public class StrategyContainerFactoryBean<T,V extends Annotation> implements FactoryBean<StrategyContainer>, ApplicationContextAware {

    private Class<T> strategyClass;

    private Class<V> strategyAnnotationClass;

    private Function<V,String> identifyCodeGetter;

    private Map<String,T> strategyTable = new HashMap<>();

    public static <T,V extends Annotation> StrategyContainerFactoryBean<T,V> build(Class<T> strategyClass, Class<V> strategyAnnotationClass , Function<V,String> identifyCodeGetter) {
        StrategyContainerFactoryBean<T,V> factoryBean = new StrategyContainerFactoryBean<>();
        factoryBean.setStrategyClass(strategyClass);
        factoryBean.setStrategyAnnotationClass(strategyAnnotationClass);
        factoryBean.setIdentifyCodeGetter(identifyCodeGetter);
        return factoryBean;
    }

    @Override
    public StrategyContainer<T> getObject() throws Exception {
        Assert.notNull(strategyClass,"strategyClass must not be null");
        Assert.notNull(strategyAnnotationClass,"strategyAnnotationClass must not be null");
        Assert.notNull(identifyCodeGetter,"identifyCodeGetter must not be null");

        return new StrategyContainer<T>() {
            @Override
            public T getStrategy(String identifyCode) {
                return strategyTable.get(identifyCode);
            }

            @Override
            public void register(String identifyCode, T strategy) {
                strategyTable.put(identifyCode,strategy);
            }
        };
    }

    public void setStrategyClass(Class<T> strategyClass) {
        this.strategyClass = strategyClass;
    }

    public void setStrategyAnnotationClass(Class<V> strategyAnnotationClass) {
        this.strategyAnnotationClass = strategyAnnotationClass;
    }

    public void setIdentifyCodeGetter(Function<V, String> identifyCodeGetter) {
        this.identifyCodeGetter = identifyCodeGetter;
    }

    @Override
    public Class<?> getObjectType() {
        return StrategyContainer.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String[] names = applicationContext.getBeanNamesForType(strategyClass);
        Arrays.stream(names).forEach(name->{
            T object = applicationContext.getBean(name,strategyClass);
            List<V> identifiers = Lists.newArrayList();
            identifiers.addAll(AnnotationUtils.getRepeatableAnnotations(AopUtils.getTargetClass(object), strategyAnnotationClass));
            if(!CollectionUtils.isEmpty(identifiers)){
                identifiers.forEach(i->{
                    String identifyCode = identifyCodeGetter.apply(i);
                    if(Objects.nonNull(strategyTable.putIfAbsent(identifyCode,object))){
                        throw new StrategyDuplicateException("StrategyClass="+strategyClass.getName()+",identifyCode="+identifyCode+"exist multi config");
                    }
                });
            }
        });
    }

}