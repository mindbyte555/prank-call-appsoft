package com.example.baseapp.fragments.chat.question

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fakecall.R

class QuestionAdapter(
    private val questions: List<String>,
    private val itemListener: (String, Int) -> Unit
) :
    RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {
    private var isClickable: Boolean = false

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionTextView: TextView = itemView.findViewById(R.id.questionTextView)
        private var lastClickTime: Long = 0



        init {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                isClickable = true
            }, 3000)

            // Set the click listener for the itemView
            itemView.setOnClickListener {
                if (isClickable) {

                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime >= 2000) {
                        lastClickTime = currentTime
                        val position = adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            itemListener(questions[position], position)
                        }


                    }
                }
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.question_item, parent, false)
        return QuestionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val currentQuestion = questions[position]
        holder.questionTextView.text = currentQuestion
    }

    override fun getItemCount() = questions.size
}
