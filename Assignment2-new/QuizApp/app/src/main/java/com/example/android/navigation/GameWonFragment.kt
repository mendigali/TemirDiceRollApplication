package com.example.android.navigation

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentGameWonBinding

class GameWonFragment : Fragment() {
    lateinit var score: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentGameWonBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_game_won, container, false
        )

        binding.results = this

        binding.nextMatchButton.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(GameWonFragmentDirections.actionGameWonFragmentToGameFragment())
        }

        val args = GameWonFragmentArgs.fromBundle(requireArguments())
        score = "${args.numCorrect} correct answers out of ${args.numQuestions} questions"

        setHasOptionsMenu(true)
        return binding.root
    }

}
