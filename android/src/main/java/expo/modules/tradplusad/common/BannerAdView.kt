package expo.modules.tradplusad.common

import android.content.Context
import android.util.Log
import android.widget.FrameLayout

class BannerAdView(context: Context): FrameLayout(context) {
    private var placementId: String? = null
    private var propsChange: Boolean = false

    private var autoDestroy: Boolean = true
    private var closeAutoShow: Boolean = false

    private var entryAdScenario: String = ""

    fun setPlacementId(placementId: String?) {
        this.placementId = placementId
    }

    fun getPlacementId(): String? {
        return this.placementId
    }

    fun setPropsChanged(propsChange: Boolean) {
        this.propsChange = propsChange
    }

    fun getPropsChanged(): Boolean {
        return this.propsChange
    }

    fun setAutoDestroy(autoDestroy: Boolean) {
        this.autoDestroy = autoDestroy
    }

    fun getAutoDestroy(): Boolean {
        return this.autoDestroy
    }

    fun setCloseAutoShow(closeAutoShow: Boolean) {
        this.closeAutoShow = closeAutoShow
    }

    fun getCloseAutoShow(): Boolean {
        return this.closeAutoShow
    }

    fun setEntryAdScenario(entryAdScenario: String) {
        this.entryAdScenario = entryAdScenario
    }

    fun getEntryAdScenario(): String {
        return this.entryAdScenario
    }

    init {
        viewTreeObserver.addOnGlobalLayoutListener { requestLayout() }
    }

    override fun requestLayout() {
        super.requestLayout()
        post {
            measure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            )
            layout(0, 0, width, height)
        }
    }
}