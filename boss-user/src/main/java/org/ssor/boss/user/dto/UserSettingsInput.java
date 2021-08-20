package org.ssor.boss.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Setter
@Getter
public class UserSettingsInput {
    @Max(3)
    @Min(0)
    private int transactionAlerts;
    @Max(3)
    @Min(0)
    private int balanceAlerts;
}
