package com.ukm.iotsamplesender

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ukm.iotsamplesender.adapter.SensorAdapter
import com.ukm.iotsamplesender.databinding.FragmentFirstBinding
import com.ukm.iotsamplesender.model.CCTVonModel
import com.ukm.iotsamplesender.model.SensorPIR

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!
    private lateinit var firestore: FirebaseFirestore
    private lateinit var ref: DocumentReference
    private lateinit var SensorPIRLampuRef: CollectionReference
    private lateinit var SensorPIRNotifikasiRef: CollectionReference
    private val SensorPIRLampu by lazy {
        SensorAdapter("Lamp", requireContext())
    }
    private val SensorPIRNotifikasi by lazy {
        SensorAdapter("Notification", requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firestore = Firebase.firestore
        ref = firestore.collection("CCTV").document("CCTVonModel")
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initCOllectionRef() {
        SensorPIRLampuRef = firestore.collection("SensorPIRLampuHistory")
        SensorPIRNotifikasiRef = firestore.collection("SensorPIRNotifikasiHistory")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCOllectionRef()
        observeCCTVonModel()
        observeSensorPIRLampu()
        observeSensorPIRNotifikasi()
        binding.recyclerPIRLampu.adapter = SensorPIRLampu
        binding.recyclerPIRNotifikasi.adapter = SensorPIRNotifikasi
        binding.buttonFirst.setOnClickListener {
            ref.update("startTask", true)
        }
    }



    private fun observeCCTVonModel() {
        ref.addSnapshotListener { value, error ->
            if (error == null) {
                val data = value?.toObject(CCTVonModel::class.java)
                binding.buttonFirst.isEnabled = data == null || data.startTask == false
            }
        }
    }

    private fun observeSensorPIRLampu() {
        SensorPIRLampuRef.addSnapshotListener { value, error ->
            if (error == null) {
                val data = value?.map {
                    it.toObject(SensorPIR::class.java)
                }
                SensorPIRLampu.setNewInstance(data?.toMutableList())
            }
        }
    }

    private fun observeSensorPIRNotifikasi() {
        SensorPIRNotifikasiRef.addSnapshotListener { value, error ->
            if (error == null) {
                val data = value?.map {
                    it.toObject(SensorPIR::class.java)
                }
                SensorPIRNotifikasi.setNewInstance(data?.toMutableList())
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}