package io.github.shengchaojie.springstrategy.test.demo.rewardpoints;

import io.github.shengchaojie.springstrategy.StrategyIdentifier;

/**
 * @author shengchaojie
 * @date 2019-07-30
 **/
@StrategyIdentifier(identifyCode = "MEDIUM")
public class MediumLevelPointsRewardStrategy implements PointsRewardStrategy{
    public Integer rewardPoints(Integer price) {
        System.out.println("返百分之10积分");
        return price /10000 * 10;
    }
}
