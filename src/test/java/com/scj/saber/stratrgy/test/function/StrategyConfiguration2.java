package com.scj.saber.stratrgy.test.function;

import com.google.common.base.Joiner;
import com.scj.saber.strategy.StrategyManagerFactoryBean;
import com.scj.saber.stratrgy.test.function.common.HelloStrategy;
import com.scj.saber.stratrgy.test.function.common.People;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shengchaojie
 * @date 2019-08-01
 **/
@Configuration
public class StrategyConfiguration2 {

    @Bean
    public StrategyManagerFactoryBean<HelloStrategy, People> helloStrategyManager(){
        StrategyManagerFactoryBean<HelloStrategy, People> factoryBean = new StrategyManagerFactoryBean<>();
        factoryBean.setStrategyClass(HelloStrategy.class);
        factoryBean.setStrategyAnnotationClass(People.class);
        factoryBean.setIdentifyCodeGetter(a -> Joiner.on(",").join(a.district(),a.gender().name()));
        return factoryBean;
    }

}