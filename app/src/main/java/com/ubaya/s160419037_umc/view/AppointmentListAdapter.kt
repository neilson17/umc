package com.ubaya.s160419037_umc.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ubaya.s160419037_umc.R
import com.ubaya.s160419037_umc.model.Appointment
import com.ubaya.s160419037_umc.util.loadImage
import kotlinx.android.synthetic.main.appointment_list_item.view.*

class AppointmentListAdapter(val appointmentList: ArrayList<Appointment>) : RecyclerView.Adapter<AppointmentListAdapter.AppointmentViewHolder>() {
    class AppointmentViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.appointment_list_item, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointmentList[position]
        with (holder.view){
            textDateAppointmentList.text = appointment.time
            textDoctorNameAppointmentList.text = appointment.doctor.name
            textDoctorCategoryAppointmentList.text = appointment.doctor.doctor_category
            imageAppointmentList.loadImage(appointment.doctor.photo_url, progressLoadAppointmentList)
            buttonInfoAppointmentList.setOnClickListener {
                val action = AppointmentsFragmentDirections.actionDoctorDetailFragmentFromAppointments(appointment.doctor.id)
                Navigation.findNavController(it).navigate(action)
            }
            buttonCallAppointmentList.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                with (builder) {
                    setMessage("${appointment.doctor.name} will call you back in a minute!\nSorry for the inconvenience :(")
                    setTitle("Call Failed")
                    setPositiveButton("OK", null)
                    create().show()
                }
            }
            buttonMessageAppointmentList.setOnClickListener {
                Toast.makeText(context, "${appointment.doctor.name} says Hi to you!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount() = appointmentList.size

    fun updateAppointmentList(newList : ArrayList<Appointment>){
        appointmentList.clear()
        appointmentList.addAll(newList)
        notifyDataSetChanged()
    }
}