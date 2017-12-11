package com.example.chidozie.justcoffees;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int numberOfCoffee = 2;
    Boolean cream = false;
    Boolean chocolate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        int price = calculatePrice(numberOfCoffee);
        String priceMessage = getOrderSummary(price);
        if (priceMessage.isEmpty()) {
            Toast maxOrder = Toast.makeText(this, getString(R.string.name_toast), Toast.LENGTH_SHORT);
            maxOrder.show();
            return;
        }
        composeEmail(priceMessage);
    }

    public void changeQuantity(View view) {
        String quantityText = view.getTag().toString();
        int action = Integer.parseInt(quantityText);
        if (action == 1) {
            if (numberOfCoffee == 100) {
                Toast maxOrder = Toast.makeText(this, getString(R.string.max_order_toast), Toast.LENGTH_SHORT);
                maxOrder.show();
                return;
            }
            numberOfCoffee = numberOfCoffee + 1;
        } else if (action == 0) {
            if (numberOfCoffee == 1) {
                Toast minOrder = Toast.makeText(this, getString(R.string.min_order_toast), Toast.LENGTH_SHORT);
                minOrder.show();
                return;
            }
            numberOfCoffee = numberOfCoffee - 1;
        }
        TextView quantityTextView = findViewById(R.id.quantity_textview);
        quantityTextView.setText(getString(R.string.quantity_view_value, numberOfCoffee));
        int price = calculatePrice(numberOfCoffee);
        displayMessage(getString(R.string.order_summary_value, price));
    }

    /**
     * This method displays the given price on the screen.
     */
    private int calculatePrice(int number) {
        getToppings();
        int pricePerCoffee = 5;
        if (cream)
            pricePerCoffee += 2;
        if (chocolate)
            pricePerCoffee += 1;
        return pricePerCoffee * number;
    }

    private void displayMessage(String message) {
        TextView quantityTextView = findViewById(R.id.summary_textview);
        quantityTextView.setText(message);
    }

    private String getOrderSummary(int price) {
        EditText nameField = findViewById(R.id.name);
        String name = nameField.getText().toString();
        if (name.isEmpty())
            return "";

        String summary = getString(R.string.summary_start, name, numberOfCoffee);
        if (cream)
            summary += getString(R.string.whipped_cream_topping);
        if (chocolate)
            summary += getString(R.string.chocolate_topping);
        if (!chocolate && !cream)
            summary += getString(R.string.no_topping);
        summary += getString(R.string.summary_end, price);
        return summary;
    }

    private void getToppings() {
        CheckBox toppingCream = findViewById(R.id.toppingsCream);
        cream = toppingCream.isChecked();
        CheckBox toppingChocolate = findViewById(R.id.toppingsChocolate);
        chocolate = toppingChocolate.isChecked();
    }

    public void composeEmail(String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "KenzDozz@aol.com", null));
        //intent.setData(Uri.fromParts("mailto"));
        //intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_subject));
        intent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(intent, getString(R.string.chooser_msg)));
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }else {
//            Toast.makeText(this, "No Email App Found", Toast.LENGTH_SHORT).show();
//        }
    }

    public void changeToppings(View view) {
        getToppings();
        int price = calculatePrice(numberOfCoffee);
        displayMessage(getString(R.string.order_summary_value, price));
    }
}