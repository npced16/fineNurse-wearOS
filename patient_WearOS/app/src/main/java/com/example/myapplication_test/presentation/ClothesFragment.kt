package com.example.myapplication_test.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import io.ktor.client.*
import io.ktor.client.engine. android.*
import io.ktor.client.features.ContentNegotiation
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.myapplication_test.R
import java.net.Socket
import java.io.OutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClothesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClothesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_clothes, container, false)

        val notificationTextView: TextView = view.findViewById(R.id.clothesNotificationTextView)
        val clothesButton: Button = view.findViewById(R.id.clothes_button)

        notificationTextView.visibility = View.GONE

        clothesButton.setOnClickListener{
            performSocketCommunication(notificationTextView)

        }

        return view
    }

    private fun performSocketCommunication(notificationTextView: TextView) {

        GlobalScope.launch(Dispatchers.IO){
            try {
                val socket = Socket("server_ip", server_port)
                val outputStream: OutputStream = socket.getOutputStream()
                val message = "환자복 요청 메시지"
                outputStream.write(message.toByteArray())

                withContext(Dispatchers.Main){
                    notificationTextView.text = "간호사에게 알림을 보냈습니다."
                }
                socket.close()
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ClothesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ClothesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
