package com.teamcity.ui.pages.favorites;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.Selectors;
import com.teamcity.ui.pages.Page;
import com.teamcity.ui.Selectors;
import com.teamcity.ui.pages.Page;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.element;

public class FavoritesPage extends Page {
    private SelenideElement header = Selenide.element(Selectors.byClass("ProjectPageHeader__title--ih"));


    protected void waitUntilFavoritesPageIsLoaded(){
        waitUntilPageIsLoaded();
        header.shouldBe(Condition.visible, Duration.ofSeconds(10));
    }

}
