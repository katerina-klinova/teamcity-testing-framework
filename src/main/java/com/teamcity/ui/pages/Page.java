package com.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.Selectors;
import com.teamcity.ui.elements.PageElement;
import lombok.Getter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.codeborne.selenide.Selenide.element;

public abstract class Page {
    @Getter protected SelenideElement header = element(com.codeborne.selenide.Selectors.byId("header"));
    private SelenideElement submitButton = element(Selectors.byType("submit"));
    protected SelenideElement savingWaitingMarker = element(Selectors.byType("saving"));
    protected static SelenideElement pageLoadingMarker = element(Selectors.byDataTest("ring-loader"));

    public void submit() {
        submitButton.click();
        waitUntilDataIsSaved();
    }

    public void waitUntilPageIsLoaded() {
        pageLoadingMarker.shouldNotBe(Condition.visible, Duration.ofMinutes(5));
    }

    public void waitUntilDataIsSaved() {
        savingWaitingMarker.shouldNotBe(Condition.visible, Duration.ofMinutes(5));
    }

    public void waitUntilElementIsVisible(SelenideElement element){
        element.shouldBe(Condition.visible, Duration.ofMinutes(5));
    }

    public void waitUntilElementIsEnabled(SelenideElement element){
        element.shouldBe(Condition.enabled, Duration.ofMinutes(5));
    }

    public <T extends PageElement> List<T> generatePageElements(
            ElementsCollection collection,
            Function<SelenideElement, T> creator) {
        var elements = new ArrayList<T>();
        collection.forEach(webElement -> elements.add(creator.apply(webElement)));
        return elements;
    }
}
