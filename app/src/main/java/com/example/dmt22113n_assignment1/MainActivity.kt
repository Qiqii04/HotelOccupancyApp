package com.example.dmt22113n_assignment1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import java.lang.NumberFormatException
import kotlin.math.floor

class MainActivity : AppCompatActivity() {
    private lateinit var button_save: Button
    private lateinit var button_restart: Button
    private lateinit var button_exit: Button

    private lateinit var spinner_floor: Spinner
    private lateinit var text_inputroom: EditText

    private lateinit var display_data: TextView
    private lateinit var display_total: TextView
    private lateinit var display_rate: TextView
    //declare a variable to store total rooms entries
    private var totalOccupiedRooms = 0
    // Variable to store the sum of individual occupancy rates
    private var totalOccupancyRate = 0.0
    private var totalRooms = 30 //Number of rooms on each floor
    private val enteredFloors = mutableSetOf<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //link to the GUI elements (set the references)
        setupGUI()

        //function to set up listener
        setupListener()
    }

    private fun setupListener() {
        button_save.setOnClickListener {
            // Get selected floor position from spinner
            var floorPosition = getFloorPosition() // Starting at floor 1

            // Check if this floor has already been entered
            if (enteredFloors.any { it == floorPosition }) {
                // Display an error message or handle it as needed
                val errorMessage = "Floor $floorPosition has already been entered."
                text_inputroom.error = errorMessage
                return@setOnClickListener
            }

            //Get the user input as a string
            val roomInput = text_inputroom.text.toString()

            //Check if the input is empty
            if (roomInput.isBlank()) {
                //display error message if empty
                val errorMessage = getString(R.string.error_empty)
                text_inputroom.error = errorMessage
            } else {
                try {
                    //if its not empty, convert room input to integer and store in roomNumber
                    val roomNumber = roomInput.toInt()

                    //check if room entered exceeds maximum (30) and display error message
                    if (roomNumber > totalRooms) {
                        val errorMessage = getString(R.string.error_max)
                        text_inputroom.error = errorMessage

                    } else {
                        //continue to calculate the occupancy rate
                        val occupancyRate = (roomNumber.toFloat() / totalRooms) * 100

                        //Update text views and display data line by line
                        //formattedRate store formatted version of occupancy rate
                        val formattedRate = String.format("%.2f", occupancyRate)
                        val floorData = "Floor: $floorPosition, Rooms Occupied: $roomNumber , Occupancy Rate: $formattedRate%"

                        //append is method used for adding new line of occupancy data to display data view
                        display_data.append(floorData + "\n")

                        //calculate total occupied rooms and display
                        totalOccupiedRooms += roomNumber
                        display_total.text = "$totalOccupiedRooms"

                        //add the current floor's occupancy rate to the total occupancy rate
                        totalOccupancyRate += occupancyRate

                        //count the number of lines that are in display_data
                        //subtract 1 for zero based indexing
                        val lineCount = (display_data.lineCount) - 1

                        //calculate and update the overall occupancy rate textviews
                        val overall = totalOccupancyRate / lineCount
                        val formattedOverall = String.format("%.2f", overall)
                        display_rate.text = "$formattedOverall%"

                        //set the selection of spinner to floor position
                        spinner_floor.setSelection(floorPosition)

                        //add the entered floor position to the set
                        enteredFloors.add(floorPosition)

                        //after all process have done, clear the text
                        //this allow user to enter data again easily
                        text_inputroom.text.clear()
                    }
                } catch (e: NumberFormatException) {
                    //Display error message for non-integer input
                    val errorMessage = getString(R.string.error_int)
                    text_inputroom.error = errorMessage
                }
            }
        }

        button_exit.setOnClickListener {
            //exit app when clicked
            finish()
        }

        button_restart.setOnClickListener {
            //reset all fields
            totalOccupiedRooms = 0
            text_inputroom.text.clear()
            display_data.text = ""
            display_total.text = ""
            display_rate.text = ""

            //Clear the set of entered floors
            enteredFloors.clear()

            //reset spinner back to 1 - first item
            spinner_floor.setSelection(0)
        }
    }

    private fun getFloorPosition(): Int{
        //get the selected floor from spinner
        //add 1 because zero based indexing, floor start from 1
        return spinner_floor.selectedItemPosition + 1
    }


    private fun setupGUI() {
        button_save = findViewById(R.id.button_save)
        button_restart = findViewById(R.id.button_restart)
        button_exit = findViewById(R.id.button_exit)

        spinner_floor = findViewById(R.id.spinner_floor)
        text_inputroom = findViewById(R.id.text_inputroom)

        display_data = findViewById(R.id.display_data)
        display_total = findViewById(R.id.display_total)
        display_rate = findViewById(R.id.display_rate)
    }
}