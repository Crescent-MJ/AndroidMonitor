package com.lygttpod.monitor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.monitor.R
import com.example.monitor.databinding.FragmentMonitorRequestBinding
import com.lygttpod.monitor.data.MonitorData
import com.lygttpod.monitor.utils.formatBody


class MonitorRequestFragment : Fragment() {

    companion object {
        fun newInstance(monitorData: MonitorData?): MonitorRequestFragment {
            return MonitorRequestFragment().apply {
                this.monitorData = monitorData
            }
        }
    }

    private var monitorData: MonitorData? = null

    private lateinit var binding: FragmentMonitorRequestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_monitor_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMonitorRequestBinding.bind(view)
        initData()
    }

    private fun initData() {
        binding.tvUrl.text = monitorData?.url
        binding.tvMethod.text = monitorData?.method
        binding.tvRequestDate.text = monitorData?.requestTime
        binding.tvHeader.text = if (monitorData?.source == "Flutter") formatBody(
            monitorData?.requestHeaders ?: "",
            "json"
        ) else monitorData?.requestHeaders
        if (monitorData?.requestBody.isNullOrBlank()) return
        binding.tvRequestBody.text = formatBody(
            monitorData?.requestBody
                ?: return, monitorData?.requestContentType
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        monitorData = null
    }
}