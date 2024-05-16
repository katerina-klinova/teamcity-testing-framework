package com.example.teamcity.ui.pages.favorites;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.elements.ProjectElement;

import java.util.List;

import static com.codeborne.selenide.Selenide.elements;

public class ProjectsPage  extends FavoritesPage {
    private static final String FAVOURITE_PROJECTS = "/favorite/projects";
    private ElementsCollection subprojects = elements(Selectors.byClass("Subproject__container--Px"));

    public ProjectsPage open(){
        Selenide.open(FAVOURITE_PROJECTS);
        waitUntilFavoritesPageIsLoaded();
        return this;
    }

    public List<ProjectElement> getSubprojects(){
        return generatePageElements(subprojects, ProjectElement::new);
    }


}