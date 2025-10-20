package expo.modules.tradplusad.common

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.tradplus.ads.open.nativead.TPNative
import expo.modules.tradplusad.utils.ReactNativeModule

class ReactNativeNativeAdView(context: Context): FrameLayout(context) {

    // request
    internal var request: ReactNativeModule.Native? = null
    internal var showAd: Boolean = false
    internal var cacheView: ViewGroup? = null

    // registerLayoutId
    internal var titleID: Int? = null
    internal var subtitleID: Int? = null
    internal var ctaID: Int? = null
    internal var iconID: Int? = null
    internal var mediaID: Int? = null
    internal var choiceID: Int? = null
    internal var choiceIconID: Int? = null
    //

    // internal
    internal var unitId: String? = null
    internal var propsChanged = false
    internal var validator = false
    // internal

    // TPBaseAd
    internal var baseAd: TPNative? = null


    override fun requestLayout() {
        super.requestLayout()
        post(measureAndLayout)
    }

    private val measureAndLayout = Runnable {
        measure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
        layout(left, top, right, bottom)
    }

    fun scaleFit(child: View?, parent: View?) {
        if (child != null && parent != null) {
           child.measure(
               MeasureSpec.makeMeasureSpec(parent.width, MeasureSpec.EXACTLY),
               MeasureSpec.makeMeasureSpec(parent.height, MeasureSpec.EXACTLY)
           )
           child.layout(parent.left, parent.top, parent.right, parent.bottom)
        }
    }
}