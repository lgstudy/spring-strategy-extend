package io.github.shengchaojie.springstrategy.test.function.multi;

import io.github.shengchaojie.springstrategy.StrategyIdentifier;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author shengchaojie
 * @date 2019-09-19
 **/
@StrategyIdentifier(identifyCode = "2")
@StrategyIdentifier(identifyCode = "3")
@Order(-1)
public class CValidation implements Validation , Ordered {
    @Override
    public void validate() {
        System.out.println("CCCCC");
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
