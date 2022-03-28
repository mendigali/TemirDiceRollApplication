package com.example.android.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentGameBinding

//import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment : Fragment() {
    data class Question(
        val text: String = "What is the name of these programming language?",
        val answers: List<String>,
        val image: Int
    )

//    private var _binding: ResultProfileBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
//    private val binding get() = _binding!!

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
        )
    )


    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    var questionIndex = 0
    val numQuestions = questions.size

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        _binding = ResultProfileBinding.inflate(inflater, container, false)
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

                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    questionIndex++
                    if (questionIndex < numQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.questionImage.setImageResource(currentQuestion.image)
                        binding.invalidateAll()
                    } else {
                        view.findNavController()
                            .navigate(
                                GameFragmentDirections
                                    .actionGameFragmentToGameWonFragment(
                                        numQuestions,
                                        questionIndex
                                    )
                            )
                    }
                } else {
                    view.findNavController()
                        .navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment())
                }
            }
        }
        return binding.root
    }

    private fun randomizeQuestions() {
//        questions.shuffle()
        questionIndex = 0
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
