package com.viartemev.requestmapper

import com.intellij.ide.util.gotoByName.CustomMatcherModel
import com.intellij.ide.util.gotoByName.FilteringGotoByModel
import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.viartemev.requestmapper.model.Path
import java.net.URL

class RequestMappingModel(project: Project) : FilteringGotoByModel<FileType>(project, arrayOf<ChooseByNameContributor>(RequestMappingContributor())), DumbAware, CustomMatcherModel {

    override fun filterValueFor(item: NavigationItem): FileType? = null

    override fun getPromptText(): String = "Enter mapping url"

    override fun getNotInMessage(): String = "No matches found"

    override fun getNotFoundMessage(): String = "Mapping not found"

    override fun getCheckBoxName(): String? = null

    override fun getCheckBoxMnemonic(): Char = 0.toChar()

    override fun loadInitialCheckBoxState(): Boolean = false

    override fun saveInitialCheckBoxState(state: Boolean) = Unit

    override fun getSeparators(): Array<String> = emptyArray()

    override fun getFullName(element: Any): String? = getElementName(element)

    override fun willOpenEditor(): Boolean = false

    override fun matches(popupItem: String, userPattern: String): Boolean {
        return if (userPattern == "/") {
            true
        } else if (!userPattern.contains('/')) {
            userPattern in popupItem
        } else {
            Path(cleanPopupItems(popupItem)).isSimilarTo(Path(extractPath(userPattern)))
        }
    }

    private fun cleanPopupItems(popupItem: String): String {
        return popupItem.split(' ')[1]
    }

    private fun extractPath(userPattern: String): String {
        return if (userPattern.startsWith("http") || userPattern.startsWith("https")) {
            URL(userPattern).path
        } else userPattern
    }
}
