package com.example.android.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    data class Question(
        val text: String = "What is the name of these?",
        val answers: List<String>,
        val image: Int
    )

    private val questions: MutableList<Question> = mutableListOf(
        Question(
            answers = listOf("Kotlin", "Webpack", "TypeScript", "Dart"),
            image = R.drawable.kotlin
        ),
        Question(
            answers = listOf("React", "Webpack", "Kotlin", "Dart"),
            image = R.drawable.react
        ),
        Question(
            answers = listOf("Python", "Java", "Solidity", "Swift"),
            image = R.drawable.python
        ),
        Question(
            answers = listOf("Swift", "Bird", "Solidity", "Objective C"),
            image = R.drawable.swift
        ),
        Question(
            answers = listOf("Golang", "Gopher", "Scala", "TypeScript"),
            image = R.drawable.golang
        ),
        Question(
            answers = listOf("Dart", "Webpack", "TypeScript", "Golang"),
            image = R.drawable.dart
        ),
        Question(
            answers = listOf("Solidity", "Solana", "Scala", "React"),
            image = R.drawable.solidity
        ),
        Question(
            answers = listOf("Redis", "Dart", "Webpack", "Parcel"),
            image = R.drawable.redis
        ),
        Question(
            answers = listOf("Spring", "Java", "Kotlin", "MongoDB"),
            image = R.drawable.spring
        ),
        Question(
            answers = listOf("PostgreSQL", "Golang", "Pascal", "TypeScript"),
            image = R.drawable.postgresql
        ),
    )

    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    var questionIndex = 0
    var trueAnswers = 0
    val numQuestions = questions.size

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
            inflater, R.layout.fragment_game, container, false
        )

        randomizeQuestions()

        binding.game = this

        binding.submitButton.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }

                if (answers[answerIndex] == currentQuestion.answers[0]) trueAnswers++

                questionIndex++

                if (questionIndex < numQuestions) {
                    currentQuestion = questions[questionIndex]
                    setQuestion()
                    binding.questionImage.setImageResource(currentQuestion.image)
                    binding.invalidateAll()
                } else {
                    view.findNavController().navigate(
                        GameFragmentDirections
                            .actionGameFragmentToGameWonFragment(
                                numQuestions,
                                trueAnswers
                            )
                    )
                }
            }
        }
        return binding.root
    }

    private fun randomizeQuestions() {
        questionIndex = 0
        trueAnswers = 0
        setQuestion()
    }

    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        answers = currentQuestion.answers.toMutableList()
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.title_coding_quiz, questionIndex + 1, numQuestions)
    }
}
