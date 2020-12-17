package com.senla.training.knowledgeservice.dto.dto.teacher;

import com.senla.training.knowledgeservice.dto.dto.AbstractDto;
import com.senla.training.knowledgeservice.model.teacher.RewardType;

import java.math.BigDecimal;
import java.util.Objects;

public class TeacherDto extends AbstractDto {

    private String firstName;
    private String lastName;
    private BigDecimal reward;
    private RewardType rewardType;
    private Boolean isActive;

    public TeacherDto() {
        super(null);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getReward() {
        return reward;
    }

    public RewardType getRewardType() {
        return rewardType;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
    }

    public void setRewardType(RewardType rewardType) {
        this.rewardType = rewardType;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        TeacherDto that = (TeacherDto) o;
        return Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName)
                && Objects.equals(reward, that.reward)
                && rewardType == that.rewardType
                && Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName,
                reward, rewardType, isActive);
    }

    @Override
    public String toString() {
        return "TeacherDto{"
                + "firstName='" + firstName
                + ", lastName='" + lastName
                + ", reward=" + reward
                + ", rewardType=" + rewardType
                + ", active=" + isActive
                + '}';
    }
}
