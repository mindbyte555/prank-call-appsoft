package com.example.baseapp.fragments.chat.answer

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.baseapp.utils.PrefUtil
import com.example.fakecall.R
import java.text.SimpleDateFormat
import java.util.Calendar

class AnswerAdapter(private val answers: MutableList<String>,private val question: MutableList<String>) :
    RecyclerView.Adapter<AnswerAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val answerTextView: TextView = itemView.findViewById(R.id.answerview)
        val questionTextView: TextView = itemView.findViewById(R.id.questionview)
        val answertimeTextView: TextView = itemView.findViewById(R.id.answertime)
        val questiontimeTextView: TextView = itemView.findViewById(R.id.questiontime)
        val answerlottie:LottieAnimationView=itemView.findViewById(R.id.loading_animanswer)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_answer, parent, false) // Ensure correct layout file
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("hh:mm aaa")
        val formattedTime = dateFormat.format(currentTime)
        val answerItem = answers[position]
        val context = holder.itemView.context
        var sms=PrefUtil(context).getInt("smstheme",0)
        if(sms==0){
            holder.answerTextView.background =
                ContextCompat.getDrawable(context, R.drawable.answer_bg)
            holder.answerTextView.setTextColor((ContextCompat.getColor(context, R.color.black)))


        }
        else if(sms==1){
            holder.answerTextView.background =
                ContextCompat.getDrawable(context, R.drawable.whatsanswer_bg)
            holder.answerTextView.setTextColor((ContextCompat.getColor(context, R.color.whastapp_answertxt)))


        }
        else if(sms==2){
            holder.answerTextView.background =
                ContextCompat.getDrawable(context, R.drawable.messengeranswer_bg)
            holder.answerTextView.setTextColor((ContextCompat.getColor(context, R.color.whastapp_quetxt)))

        }
        else if(sms==3){
            holder.answerTextView.background =
                ContextCompat.getDrawable(context, R.drawable.messengeranswer_bg)
            holder.answerTextView.backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.white)
            holder.answerTextView.setTextColor((ContextCompat.getColor(context, R.color.whastapp_quetxt)))

        }

        else if(sms==4){
            holder.answerTextView.background =
                ContextCompat.getDrawable(context, R.drawable.messengeranswer_bg)
            holder.answerTextView.backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.messenger_ans)
            holder.answerTextView.setTextColor((ContextCompat.getColor(context, R.color.whastapp_quetxt)))

        }



        holder.answerTextView.text = answerItem
        holder.answertimeTextView.text=formattedTime.toString()
        if (position == itemCount - 1) {
            // Only the last item will have the animation and visibility toggling
            holder.answerTextView.visibility = View.GONE
            holder.answertimeTextView.visibility = View.GONE

            holder.answerlottie.visibility = View.VISIBLE
            holder.answerlottie.playAnimation()

            val handler = Handler(Looper.getMainLooper())  // Ensure you use the main looper

            val showTextRunnable = Runnable {
                holder.answerlottie.visibility = View.GONE
                holder.answerTextView.visibility = View.VISIBLE
                holder.answertimeTextView.visibility = View.VISIBLE
            }

            handler.postDelayed(showTextRunnable, 3500)
        } else {
            // For all other items, ensure they are visible or set them as per your default state
            holder.answerTextView.visibility = View.VISIBLE
            holder.answertimeTextView.visibility = View.VISIBLE
            holder.answerlottie.visibility = View.GONE
        }

        if (position < question.size) {
            if(sms==0){
                holder.questionTextView.background =
                    ContextCompat.getDrawable(context, R.drawable.question_bg)
                holder.questionTextView.setTextColor((ContextCompat.getColor(context, R.color.black)))


            }
            else if(sms==1){
                holder.questionTextView.background =
                    ContextCompat.getDrawable(context, R.drawable.whatsquestion_bg)
                holder.questionTextView.setTextColor((ContextCompat.getColor(context, R.color.whastapp_quetxt)))


            }
            else if(sms==2){
                holder.questionTextView.background =
                    ContextCompat.getDrawable(context, R.drawable.messengerquestion_bg)
                holder.questionTextView.setTextColor((ContextCompat.getColor(context, R.color.white)))

            }

            else if(sms==3){
                holder.questionTextView.background =
                    ContextCompat.getDrawable(context, R.drawable.messengerquestion_bg)
                holder.questionTextView.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.telegram_quebg)
                holder.questionTextView.setTextColor((ContextCompat.getColor(context, R.color.telegram_que)))

            }
            else if(sms==4){
                holder.questionTextView.background =
                    ContextCompat.getDrawable(context, R.drawable.messengerquestion_bg)
                holder.questionTextView.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.insta_que)
                holder.questionTextView.setTextColor((ContextCompat.getColor(context, R.color.white)))

            }
            val questionItem = question[position]
            holder.questionTextView.text = questionItem
//            holder.questionTextView.setBackgroundResource(R.drawable.question_bg)
            holder.questiontimeTextView.text=formattedTime.toString()

        } else {
            holder.questionTextView.text = ""
            holder.questionTextView.setBackgroundResource(R.color.trans)
            holder.questiontimeTextView.text=""

        }
    }


    override fun getItemCount() = answers.size
}


