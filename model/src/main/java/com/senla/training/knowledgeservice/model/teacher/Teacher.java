package com.senla.training.knowledgeservice.model.teacher;

import com.senla.training.knowledgeservice.model.AbstractEntity;
import com.senla.training.knowledgeservice.model.subscription.lesson.LessonSubscription;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "teachers")
public class Teacher extends AbstractEntity {

    @Basic
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;
    @Basic
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Basic
    @Column(name = "reward", nullable = false)
    private BigDecimal reward;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "reward_type", nullable = false, length = 45)
    private RewardType rewardType;
    @Basic
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teacher",
            fetch = FetchType.LAZY)
    private Set<LessonSubscription> lessonSubscriptions = new HashSet<>();

    public Teacher() {
    }

    public Teacher(String firstName,
                   String lastName,
                   BigDecimal reward,
                   RewardType rewardType,
                   Boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.reward = reward;
        this.rewardType = rewardType;
        this.isActive = isActive;
    }

    public Teacher(Integer id,
                   String firstName,
                   String lastName,
                   BigDecimal reward,
                   RewardType rewardType,
                   Boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.reward = reward;
        this.rewardType = rewardType;
        this.isActive = isActive;
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

    public Set<LessonSubscription> getLessonSubscriptions() {
        return lessonSubscriptions;
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

    public void setLessonSubscriptions(Set<LessonSubscription> lessonSubscriptions) {
        this.lessonSubscriptions = lessonSubscriptions;
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
        Teacher teacher = (Teacher) o;
        return Objects.equals(firstName, teacher.firstName)
                && Objects.equals(lastName, teacher.lastName)
                && Objects.equals(reward, teacher.reward)
                && rewardType == teacher.rewardType
                && Objects.equals(isActive, teacher.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, reward,
                rewardType, isActive);
    }

    @Override
    public String toString() {
        return "Teacher{"
                + "firstName='" + firstName
                + ", lastName='" + lastName
                + ", reward=" + reward
                + ", rewardType=" + rewardType
                + ", is active=" + isActive
                + ", id=" + id + '}';
    }
}
