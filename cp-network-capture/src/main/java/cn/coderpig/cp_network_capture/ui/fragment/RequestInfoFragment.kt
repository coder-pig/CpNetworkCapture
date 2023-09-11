package cn.coderpig.cp_network_capture.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.coderpig.cp_network_capture.R
import cn.coderpig.cp_network_capture.databinding.FragmentRequestInfoBinding
import cn.coderpig.cp_network_capture.entity.NetworkLog
import cn.coderpig.cp_network_capture.ui.activity.NetworkLogActivity.Companion.KEY_NETWORK_LOG
import cn.coderpig.cp_network_capture.utils.*
import cn.coderpig.cp_network_capture.utils.binding
import cn.coderpig.cp_network_capture.utils.generateHeaderView

/**
 * Author: zpj
 * Date: 2023-09-05 15:41
 * Desc: 请求信息展示页
 */
class RequestInfoFragment : Fragment() {
    companion object {
        fun newInstance(networkLog: NetworkLog) = RequestInfoFragment().apply {
            arguments = Bundle().apply { putSerializable(KEY_NETWORK_LOG, networkLog) }
        }
    }

    private val mBinding by binding(FragmentRequestInfoBinding::bind)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_request_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (arguments?.getSerializable(KEY_NETWORK_LOG) as? NetworkLog)?.let {
            mBinding?.apply {
                tvRequestUrl.text = it.url
                tvRequestMethod.text = it.method
                llyRequestHeaders.apply {
                    if (it.requestHeaders.isNullOrBlank()) {
                        tvRequestHeaderEmpty.visibility = View.VISIBLE
                    } else {
                        it.requestHeaders!!.let { headers ->
                            GsonHelper.fromJsonArray(headers, HttpHeader::class.java)
                                .forEach { header -> addView(header.generateHeaderView(requireActivity())) }
                        }
                    }
                }
                switchRequestHeadersUI(requireActivity().isFoldRequestHeaders)
                tvRequestHeadersLabel.setOnClickListener {
                    switchRequestHeadersUI(llyRequestHeadersContainer.visibility == View.VISIBLE)
                }
                tvRequestBody.text = it.requestBody.noDataOrThis()
                fbtToTop.setOnClickListener { nsvContent.smoothScrollTo(nsvContent.x.toInt(), 0) }
                nsvContent.viewTreeObserver.addOnScrollChangedListener {
                    if (nsvContent.scrollY == 0 && fbtToTop.visibility == View.VISIBLE) {
                        fbtToTop.hide()
                    } else if (nsvContent.scrollY > 0 && fbtToTop.visibility == View.GONE) {
                        fbtToTop.show()
                    }
                }
            }
        }
    }

    /**
     * 切换请求头折叠/展开状态
     * */
    private fun switchRequestHeadersUI(isFold: Boolean = false) {
        mBinding?.apply {
            if (isFold) {
                llyRequestHeadersContainer.visibility = View.GONE
                tvRequestHeadersLabel.text = getText(R.string.cp_network_capture_expand_request_headers)
            } else {
                llyRequestHeadersContainer.visibility = View.VISIBLE
                tvRequestHeadersLabel.text = getText(R.string.cp_network_capture_fold_request_headers)
            }
        }
    }

}
