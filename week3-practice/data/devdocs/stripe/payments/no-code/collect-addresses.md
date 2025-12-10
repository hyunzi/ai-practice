# Collect physical addresses and phone numbers

Learn how to collect addresses and phone numbers without writing code.

You can collect addresses and phone numbers with payment links by adding those fields to the checkout session.

#### Dashboard

### Collect an address

To collect addresses from your customers:

1. [Create](https://dashboard.stripe.com/payment-links/create) or edit a payment link.

   To edit a payment link go to its details page and click the overflow menu (⋯).

1. Select **Collect customers’ addresses** in the **Options** section.

1. You  can collect **Billing addresses only** or you can collect **Billing and shipping addresses**. Choosing either makes these fields required for customers.

1. If you collect shipping addresses:

   - You need to select the countries you ship to. These countries appear in the **Country** dropdown in the **Shipping Address form** in the checkout session.
   - You can optionally add shipping rates.

### Collect a phone number

If you need to collect phone numbers to complete the transaction:

1. [Create](https://dashboard.stripe.com/payment-links/create) or edit a payment link.
1. Select **Require customers to provide a phone number**.

#### API

### Collect an address

To collect a customer’s billing address in a payment link, pass the `billing_address_collection` parameter when you [create a payment link](https://docs.stripe.com/api/payment_links/payment_links/create.md). You have to specify whether to always collect the billing address (`required`) or only when it’s necessary (like for tax calculations) (`auto`).

To collect a customer’s shipping address in a payment link, pass the `shipping_address_collection` parameter when you [create a payment link](https://docs.stripe.com/api/payment_links/payment_links/create.md).

When you collect a shipping address, you must also specify which countries to allow shipping to. Configure the `allowed_countries` property with an array of [two-letter ISO country codes](https://www.nationsonline.org/oneworld/country_code_list.htm).

When the customer completes the session, the [Checkout session](https://docs.stripe.com/api/checkout/sessions/object.md) object saves the collected shipping address on the `shipping_details` property and includes it in the payload of the `checkout.session.completed` *webhook* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests). You can also see shipping information in the payment details page in the Dashboard.

### Collect a phone number

To collect a customer’s phone number in a payment link, pass the `phone_number_collection` parameter when you [create a payment link](https://docs.stripe.com/api/payment_links/payment_links/create.md).
