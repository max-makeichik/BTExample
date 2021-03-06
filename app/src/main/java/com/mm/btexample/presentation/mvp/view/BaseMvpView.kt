package com.mm.btexample.presentation.mvp.view

import com.arellomobile.mvp.MvpView

/**
 * Interface representing a View that will use to load data.
 */
interface BaseMvpView : MvpView {

    /**
     * Show a view with a progress bar indicating a pic_spinner_orange process.
     */
    fun showLoading()

    /**
     * Hide a pic_spinner_orange view.
     */
    fun hideLoading()

    /**
     * Error view
     *
     * @param errorMessage - error message
     */
    fun showError(errorMessage: String)

    /**
     * Error view hide.
     */
    fun hideError()
}
