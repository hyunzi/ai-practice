# How cards work

Learn how an online credit or debit card payment works.

For information on payment method transaction fees, see [local payment method pricing](https://stripe.com/pricing/local-payment-methods).

[Cards](https://docs.stripe.com/payments/cards.md) are one of the most popular ways to pay online, with broad global reach. There are different types of cards and several steps in the process. To build a Stripe integration that supports all of your customers, see what goes on behind the scenes of a card payment.

Stripe checks that the details provided are formatted correctly (for example, the expiry date isn’t in the past). There’s no guarantee that the card itself is valid yet.

Some banks, especially in regulated regions like Europe and India, might prompt the customer to authenticate a purchase. For example, the customer might receive a text with a code to enter on the bank’s website.

The bank checks for sufficient funds and, if successful, holds the amount on the customer’s account to guarantee it for the Stripe user.

The money moves from the issuing bank to the Stripe user’s account.

## Card updates 

[Updating a saved card](https://docs.stripe.com/api/cards/update.md) can only change its name, billing address, expiration date, or metadata. To make any other changes, you must delete the card and create a new one.

To let your customers manage their own payment methods, implement processes that allow them to manually update and replace their saved cards.

To change a customer’s default payment method for invoices and subscriptions, make an API call to [update customer](https://docs.stripe.com/api.md#update_customer) and provide a new value for the `invoice_settings.default_payment_method` property.

```curl
curl https://api.stripe.com/v1/customers/cus_V9T7vofUbZMqpv \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "invoice_settings[default_payment_method]"=pm_1Msy7wLkdIwHu7ixsxmFvcz7
```

```cli
stripe customers update cus_V9T7vofUbZMqpv \
  -d "invoice_settings[default_payment_method]"=pm_1Msy7wLkdIwHu7ixsxmFvcz7
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

customer = client.v1.customers.update(
  'cus_V9T7vofUbZMqpv',
  {invoice_settings: {default_payment_method: 'pm_1Msy7wLkdIwHu7ixsxmFvcz7'}},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
customer = client.v1.customers.update(
  "cus_V9T7vofUbZMqpv",
  {"invoice_settings": {"default_payment_method": "pm_1Msy7wLkdIwHu7ixsxmFvcz7"}},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$customer = $stripe->customers->update(
  'cus_V9T7vofUbZMqpv',
  ['invoice_settings' => ['default_payment_method' => 'pm_1Msy7wLkdIwHu7ixsxmFvcz7']]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CustomerUpdateParams params =
  CustomerUpdateParams.builder()
    .setInvoiceSettings(
      CustomerUpdateParams.InvoiceSettings.builder()
        .setDefaultPaymentMethod("pm_1Msy7wLkdIwHu7ixsxmFvcz7")
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Customer customer = client.v1().customers().update("cus_V9T7vofUbZMqpv", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const customer = await stripe.customers.update(
  'cus_V9T7vofUbZMqpv',
  {
    invoice_settings: {
      default_payment_method: 'pm_1Msy7wLkdIwHu7ixsxmFvcz7',
    },
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CustomerUpdateParams{
  InvoiceSettings: &stripe.CustomerUpdateInvoiceSettingsParams{
    DefaultPaymentMethod: stripe.String("pm_1Msy7wLkdIwHu7ixsxmFvcz7"),
  },
}
result, err := sc.V1Customers.Update(context.TODO(), "cus_V9T7vofUbZMqpv", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CustomerUpdateOptions
{
    InvoiceSettings = new CustomerInvoiceSettingsOptions
    {
        DefaultPaymentMethod = "pm_1Msy7wLkdIwHu7ixsxmFvcz7",
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Customers;
Customer customer = service.Update("cus_V9T7vofUbZMqpv", options);
```

For information on how Checkout handles saved payment methods, see the [create session](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-customer) API reference. To consider default payment methods in other scenarios, use custom code.

## Automatic card updates 

Saved payment method details can continue to work even if the issuing bank replaces the physical card. Stripe works with *card networks* (A network that processes the transactions of a particular card brand. It might be an intermediary in front of an issuing bank as with Visa or Mastercard, or a standalone entity as with American Express) and automatically attempts to update saved card details whenever a customer receives a new card (for example, replacing an expired card or one that was reported lost or stolen). This allows your customers to continue using your service without interruption and reduces the need for you to collect new card details whenever a card is replaced.

Automatic card updates require *card issuers* (The entity that issued a payment card to a cardholder. This could be a bank, such as with the Visa or Mastercard network, or it could be the card network itself, such as with American Express) to participate with the network and provide this information. It’s widely supported in the United States, allowing Stripe to automatically update most American Express, Visa, Mastercard, and Discover cards issued there. International support varies from country to country. It isn’t possible to identify cards that support automatic updates.

You can listen for Stripe *webhooks* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests) to learn of card update activity:

- The `payment_method.updated` event notifies you of updates to a card through an API call.
- The `payment_method.automatically_updated` event notifies you of automatic card updates from the network.

These events include the card’s new expiration date and last four digits, so you can update your own records as needed. If the card update includes a new card number, the [fingerprint](https://docs.stripe.com/api/payment_methods/object.md#payment_method_object-card-fingerprint) changes.

## See also

- [Cards](https://docs.stripe.com/payments/cards.md)
- [Co-badged cards compliance](https://docs.stripe.com/co-badged-cards-compliance.md)
- [Payment method integration options](https://docs.stripe.com/payments/payment-methods/integration-options.md)
