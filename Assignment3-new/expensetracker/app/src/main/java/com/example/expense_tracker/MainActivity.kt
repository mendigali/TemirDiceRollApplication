package com.example.expense_tracker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var deletedTransaction: Transaction
    private lateinit var transactions: List<Transaction>
    private lateinit var oldtransactions: List<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var linearlayoutManager: LinearLayoutManager
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transactions = arrayListOf()
        transactionAdapter = TransactionAdapter(transactions)
        linearlayoutManager = LinearLayoutManager(this)

        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "transactions"
        ).build()

        recyclerview.apply {
            adapter = transactionAdapter
            layoutManager = linearlayoutManager
        }

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTransaction(transactions[viewHolder.adapterPosition])
            }

        }
        val swipeHelper = ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(recyclerview)

        addBtn.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchAll() {
        GlobalScope.launch {

            transactions = db.transactionDao().getAll()

            runOnUiThread {
                updateDashboard()
                transactionAdapter.setData(transactions)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateDashboard() {
        val totalAmount = transactions.sumOf { it.amount }
        val budgetAmount = transactions.filter { it.amount > 0 }.sumOf { it.amount }
        val expenseAmount = totalAmount - budgetAmount

        balance.text = "$ %.2f".format(totalAmount)
        budget.text = "$ %.2f".format(budgetAmount)
        expense.text = "$ %.2f".format(expenseAmount)
    }

    private fun undoDelete() {
        GlobalScope.launch {
            db.transactionDao().insertAll(deletedTransaction)

            transactions = oldtransactions

            runOnUiThread {
                transactionAdapter.setData(transactions)
                updateDashboard()
            }
        }
    }

    private fun showSnakbar() {
        val view = findViewById<View>(R.id.coordinator)
        val snackbar = Snackbar.make(view, "Trabsaction deleted!", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            undoDelete()
        }
            .setActionTextColor(ContextCompat.getColor(this, R.color.red))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()
    }

    private fun deleteTransaction(transaction: Transaction) {
        deletedTransaction = transaction
        oldtransactions = transactions

        GlobalScope.launch {
            db.transactionDao().delete(transaction)

            transactions = transactions.filter { it.id != transaction.id }
            runOnUiThread {
                updateDashboard()
                transactionAdapter.setData(transactions)
                showSnakbar()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        fetchAll()
    }
}