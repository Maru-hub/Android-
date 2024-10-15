package jp.ac.neec.it.k021c1460.myspiapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * A simple [Fragment] subclass.
 * Use the [examMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class examMainFragment : Fragment(R.layout.fragment_exam_main) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val question = view.findViewById<TextView>(R.id.question)
    }
}