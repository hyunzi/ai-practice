# Collect customer tax IDs with Checkout

Learn how to collect VAT and other customer tax IDs with Checkout.

# Stripe-hosted page

> This is a Stripe-hosted page for when payment-ui is stripe-hosted. View the full page at https://docs.stripe.com/tax/checkout/tax-ids?payment-ui=stripe-hosted.

Displaying a customer’s tax ID and legal business name on *invoices* (Invoices are statements of amounts owed by a customer. They track the status of payments from draft through paid or otherwise finalized. Subscriptions automatically generate invoices, or you can manually create a one-off invoice) and processing VAT refunds are common requirements that you can satisfy by enabling tax ID collection in Checkout. This guide assumes that you’ve already integrated Checkout. If you haven’t, see the [Accept a payment guide](https://docs.stripe.com/payments/accept-a-payment.md).

## Enable Tax ID collection

With tax ID collection enabled, Checkout shows and hides the tax ID collection form depending on your customer’s location. If your customer is in a location supported by tax ID collection, Checkout shows a checkbox allowing the customer to indicate that they’re purchasing as a business. When a customer checks the box, Checkout displays fields for them to enter the tax ID and legal entity name for the business. If available, Checkout uses the customer’s shipping address to determine their location, otherwise Checkout uses the customer’s billing address. Customers can only enter one tax ID.

### New Customers

To enable tax ID collection for new customers, set [tax_id_collection[enabled]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-tax_id_collection-enabled) to `true` when creating a Checkout session.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  --mode=payment \
  --success-url="https://example.com/success"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {name: 'T-shirt'},
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {enabled: true},
  mode: 'payment',
  success_url: 'https://example.com/success',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "line_items": [
    {
      "price_data": {
        "unit_amount": 1000,
        "product_data": {"name": "T-shirt"},
        "currency": "eur",
      },
      "quantity": 2,
    },
  ],
  "tax_id_collection": {"enabled": True},
  "mode": "payment",
  "success_url": "https://example.com/success",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'line_items' => [
    [
      'price_data' => [
        'unit_amount' => 1000,
        'product_data' => ['name' => 'T-shirt'],
        'currency' => 'eur',
      ],
      'quantity' => 2,
    ],
  ],
  'tax_id_collection' => ['enabled' => true],
  'mode' => 'payment',
  'success_url' => 'https://example.com/success',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setUnitAmount(1000L)
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setCurrency("eur")
            .build()
        )
        .setQuantity(2L)
        .build()
    )
    .setTaxIdCollection(
      SessionCreateParams.TaxIdCollection.builder().setEnabled(true).build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl("https://example.com/success")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {
          name: 'T-shirt',
        },
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {
    enabled: true,
  },
  mode: 'payment',
  success_url: 'https://example.com/success',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        UnitAmount: stripe.Int64(1000),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        Currency: stripe.String(stripe.CurrencyEUR),
      },
      Quantity: stripe.Int64(2),
    },
  },
  TaxIDCollection: &stripe.CheckoutSessionCreateTaxIDCollectionParams{
    Enabled: stripe.Bool(true),
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  SuccessURL: stripe.String("https://example.com/success"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                UnitAmount = 1000,
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                Currency = "eur",
            },
            Quantity = 2,
        },
    },
    TaxIdCollection = new Stripe.Checkout.SessionTaxIdCollectionOptions
    {
        Enabled = true,
    },
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

This example creates a Session in `payment` mode with tax ID collection enabled. For subscriptions, make the same changes with the [mode](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-mode) set to `subscription`.

You can additionally configure Checkout to create a new [Customer](https://docs.stripe.com/api/customers/object.md) for you using [customer_creation](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-customer_creation). If you do, Checkout saves any tax ID information collected during a Session to that new *Customer* (Customer objects represent customers of your business. They let you reuse payment methods and give you the ability to track multiple payments). If not, the tax ID information will still be available at [customer_details.tax_ids](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-customer_details-tax_ids).

### Existing Customers

If you pass an existing Customer when creating a Session, Checkout updates the Customer with any tax ID information collected during the Session. Checkout saves the collected business name onto the Customer’s [name](https://docs.stripe.com/api/customers/object.md#customer_object-name) property, and adds the collected tax ID to the Customer’s [customer.tax_ids](https://docs.stripe.com/api/customers/object.md#customer_object-tax_ids) array. Since the collection of a business name could result in the Customer’s existing [name](https://docs.stripe.com/api/customers/object.md#customer_object-name) being overridden, you must set [customer_update.name](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-customer_update-name) to `auto` when creating the Session.

> Checkout only collects tax IDs on Customers that don’t already have an existing tax ID. If a Customer has one or more tax IDs saved, Checkout doesn’t display the tax ID collection form even if tax ID collection is enabled.

When collecting tax IDs for existing customers you can either base their location on existing addresses on the customer or the addresses entered during checkout. By default, Checkout looks for existing addresses on the customer to assess their location:

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  -d "customer_update[name]"=auto \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  -d "customer_update[name]"=auto \
  --mode=payment \
  --success-url="https://example.com/success"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  customer: '{{CUSTOMER_ID}}',
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {name: 'T-shirt'},
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {enabled: true},
  customer_update: {name: 'auto'},
  mode: 'payment',
  success_url: 'https://example.com/success',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "customer": "{{CUSTOMER_ID}}",
  "line_items": [
    {
      "price_data": {
        "unit_amount": 1000,
        "product_data": {"name": "T-shirt"},
        "currency": "eur",
      },
      "quantity": 2,
    },
  ],
  "tax_id_collection": {"enabled": True},
  "customer_update": {"name": "auto"},
  "mode": "payment",
  "success_url": "https://example.com/success",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'customer' => '{{CUSTOMER_ID}}',
  'line_items' => [
    [
      'price_data' => [
        'unit_amount' => 1000,
        'product_data' => ['name' => 'T-shirt'],
        'currency' => 'eur',
      ],
      'quantity' => 2,
    ],
  ],
  'tax_id_collection' => ['enabled' => true],
  'customer_update' => ['name' => 'auto'],
  'mode' => 'payment',
  'success_url' => 'https://example.com/success',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setUnitAmount(1000L)
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setCurrency("eur")
            .build()
        )
        .setQuantity(2L)
        .build()
    )
    .setTaxIdCollection(
      SessionCreateParams.TaxIdCollection.builder().setEnabled(true).build()
    )
    .setCustomerUpdate(
      SessionCreateParams.CustomerUpdate.builder()
        .setName(SessionCreateParams.CustomerUpdate.Name.AUTO)
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl("https://example.com/success")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  customer: '{{CUSTOMER_ID}}',
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {
          name: 'T-shirt',
        },
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {
    enabled: true,
  },
  customer_update: {
    name: 'auto',
  },
  mode: 'payment',
  success_url: 'https://example.com/success',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        UnitAmount: stripe.Int64(1000),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        Currency: stripe.String(stripe.CurrencyEUR),
      },
      Quantity: stripe.Int64(2),
    },
  },
  TaxIDCollection: &stripe.CheckoutSessionCreateTaxIDCollectionParams{
    Enabled: stripe.Bool(true),
  },
  CustomerUpdate: &stripe.CheckoutSessionCreateCustomerUpdateParams{
    Name: stripe.String("auto"),
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  SuccessURL: stripe.String("https://example.com/success"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                UnitAmount = 1000,
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                Currency = "eur",
            },
            Quantity = 2,
        },
    },
    TaxIdCollection = new Stripe.Checkout.SessionTaxIdCollectionOptions
    {
        Enabled = true,
    },
    CustomerUpdate = new Stripe.Checkout.SessionCustomerUpdateOptions { Name = "auto" },
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

If you don’t have the addresses of your existing customers saved, you can base their location on the billing or shipping address entered during Checkout. To specify that you want to use the billing address entered during Checkout to assess the customer’s location, you must set [customer_update.address](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-customer_update-address) to `auto`. When setting [customer_update.address](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-customer_update-address) to `auto`, Checkout replaces any previously saved addresses on the customer with the address entered during the session.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  -d "customer_update[name]"=auto \
  -d "customer_update[address]"=auto \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  -d "customer_update[name]"=auto \
  -d "customer_update[address]"=auto \
  --mode=payment \
  --success-url="https://example.com/success"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  customer: '{{CUSTOMER_ID}}',
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {name: 'T-shirt'},
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {enabled: true},
  customer_update: {
    name: 'auto',
    address: 'auto',
  },
  mode: 'payment',
  success_url: 'https://example.com/success',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "customer": "{{CUSTOMER_ID}}",
  "line_items": [
    {
      "price_data": {
        "unit_amount": 1000,
        "product_data": {"name": "T-shirt"},
        "currency": "eur",
      },
      "quantity": 2,
    },
  ],
  "tax_id_collection": {"enabled": True},
  "customer_update": {"name": "auto", "address": "auto"},
  "mode": "payment",
  "success_url": "https://example.com/success",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'customer' => '{{CUSTOMER_ID}}',
  'line_items' => [
    [
      'price_data' => [
        'unit_amount' => 1000,
        'product_data' => ['name' => 'T-shirt'],
        'currency' => 'eur',
      ],
      'quantity' => 2,
    ],
  ],
  'tax_id_collection' => ['enabled' => true],
  'customer_update' => [
    'name' => 'auto',
    'address' => 'auto',
  ],
  'mode' => 'payment',
  'success_url' => 'https://example.com/success',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setUnitAmount(1000L)
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setCurrency("eur")
            .build()
        )
        .setQuantity(2L)
        .build()
    )
    .setTaxIdCollection(
      SessionCreateParams.TaxIdCollection.builder().setEnabled(true).build()
    )
    .setCustomerUpdate(
      SessionCreateParams.CustomerUpdate.builder()
        .setName(SessionCreateParams.CustomerUpdate.Name.AUTO)
        .setAddress(SessionCreateParams.CustomerUpdate.Address.AUTO)
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl("https://example.com/success")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  customer: '{{CUSTOMER_ID}}',
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {
          name: 'T-shirt',
        },
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {
    enabled: true,
  },
  customer_update: {
    name: 'auto',
    address: 'auto',
  },
  mode: 'payment',
  success_url: 'https://example.com/success',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        UnitAmount: stripe.Int64(1000),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        Currency: stripe.String(stripe.CurrencyEUR),
      },
      Quantity: stripe.Int64(2),
    },
  },
  TaxIDCollection: &stripe.CheckoutSessionCreateTaxIDCollectionParams{
    Enabled: stripe.Bool(true),
  },
  CustomerUpdate: &stripe.CheckoutSessionCreateCustomerUpdateParams{
    Name: stripe.String("auto"),
    Address: stripe.String("auto"),
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  SuccessURL: stripe.String("https://example.com/success"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                UnitAmount = 1000,
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                Currency = "eur",
            },
            Quantity = 2,
        },
    },
    TaxIdCollection = new Stripe.Checkout.SessionTaxIdCollectionOptions
    {
        Enabled = true,
    },
    CustomerUpdate = new Stripe.Checkout.SessionCustomerUpdateOptions
    {
        Name = "auto",
        Address = "auto",
    },
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

If you’re collecting shipping addresses for existing customers, you must base their location on the shipping address entered during checkout. To do so, set [customer_update.shipping](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-customer_update-shipping) to `auto`. When setting [customer_update.shipping](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-customer_update-shipping) to `auto`, Checkout replaces any previously saved shipping addresses on the customer with the shipping address entered during the session.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer=cus_HQmikpKnGHkNwW \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  -d "customer_update[name]"=auto \
  -d "customer_update[shipping]"=auto \
  -d "shipping_address_collection[allowed_countries][0]"=DE \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  --customer=cus_HQmikpKnGHkNwW \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  -d "customer_update[name]"=auto \
  -d "customer_update[shipping]"=auto \
  -d "shipping_address_collection[allowed_countries][0]"=DE \
  --mode=payment \
  --success-url="https://example.com/success"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  customer: 'cus_HQmikpKnGHkNwW',
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {name: 'T-shirt'},
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {enabled: true},
  customer_update: {
    name: 'auto',
    shipping: 'auto',
  },
  shipping_address_collection: {allowed_countries: ['DE']},
  mode: 'payment',
  success_url: 'https://example.com/success',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "customer": "cus_HQmikpKnGHkNwW",
  "line_items": [
    {
      "price_data": {
        "unit_amount": 1000,
        "product_data": {"name": "T-shirt"},
        "currency": "eur",
      },
      "quantity": 2,
    },
  ],
  "tax_id_collection": {"enabled": True},
  "customer_update": {"name": "auto", "shipping": "auto"},
  "shipping_address_collection": {"allowed_countries": ["DE"]},
  "mode": "payment",
  "success_url": "https://example.com/success",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'customer' => 'cus_HQmikpKnGHkNwW',
  'line_items' => [
    [
      'price_data' => [
        'unit_amount' => 1000,
        'product_data' => ['name' => 'T-shirt'],
        'currency' => 'eur',
      ],
      'quantity' => 2,
    ],
  ],
  'tax_id_collection' => ['enabled' => true],
  'customer_update' => [
    'name' => 'auto',
    'shipping' => 'auto',
  ],
  'shipping_address_collection' => ['allowed_countries' => ['DE']],
  'mode' => 'payment',
  'success_url' => 'https://example.com/success',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setCustomer("cus_HQmikpKnGHkNwW")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setUnitAmount(1000L)
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setCurrency("eur")
            .build()
        )
        .setQuantity(2L)
        .build()
    )
    .setTaxIdCollection(
      SessionCreateParams.TaxIdCollection.builder().setEnabled(true).build()
    )
    .setCustomerUpdate(
      SessionCreateParams.CustomerUpdate.builder()
        .setName(SessionCreateParams.CustomerUpdate.Name.AUTO)
        .setShipping(SessionCreateParams.CustomerUpdate.Shipping.AUTO)
        .build()
    )
    .setShippingAddressCollection(
      SessionCreateParams.ShippingAddressCollection.builder()
        .addAllowedCountry(
          SessionCreateParams.ShippingAddressCollection.AllowedCountry.DE
        )
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl("https://example.com/success")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  customer: 'cus_HQmikpKnGHkNwW',
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {
          name: 'T-shirt',
        },
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {
    enabled: true,
  },
  customer_update: {
    name: 'auto',
    shipping: 'auto',
  },
  shipping_address_collection: {
    allowed_countries: ['DE'],
  },
  mode: 'payment',
  success_url: 'https://example.com/success',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Customer: stripe.String("cus_HQmikpKnGHkNwW"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        UnitAmount: stripe.Int64(1000),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        Currency: stripe.String(stripe.CurrencyEUR),
      },
      Quantity: stripe.Int64(2),
    },
  },
  TaxIDCollection: &stripe.CheckoutSessionCreateTaxIDCollectionParams{
    Enabled: stripe.Bool(true),
  },
  CustomerUpdate: &stripe.CheckoutSessionCreateCustomerUpdateParams{
    Name: stripe.String("auto"),
    Shipping: stripe.String("auto"),
  },
  ShippingAddressCollection: &stripe.CheckoutSessionCreateShippingAddressCollectionParams{
    AllowedCountries: []*string{stripe.String("DE")},
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  SuccessURL: stripe.String("https://example.com/success"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Customer = "cus_HQmikpKnGHkNwW",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                UnitAmount = 1000,
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                Currency = "eur",
            },
            Quantity = 2,
        },
    },
    TaxIdCollection = new Stripe.Checkout.SessionTaxIdCollectionOptions
    {
        Enabled = true,
    },
    CustomerUpdate = new Stripe.Checkout.SessionCustomerUpdateOptions
    {
        Name = "auto",
        Shipping = "auto",
    },
    ShippingAddressCollection = new Stripe.Checkout.SessionShippingAddressCollectionOptions
    {
        AllowedCountries = new List<string> { "DE" },
    },
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

The above code example creates a Session in `payment` mode with tax ID collection enabled. For subscriptions, make the same changes with the [mode](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-mode) set to `subscription`.

## Optional: Require tax ID collection

You can optionally configure Checkout to require tax ID collection by setting the [tax_id_collection[required]](https://docs.stripe.com/api/.md#create_checkout_session-tax_id_collection-required) parameter. When set to `if_supported`, Checkout will require tax ID information for payment for customers in [supported billing countries](https://docs.stripe.com/tax/checkout/tax-ids.md#supported-types).

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  -d "tax_id_collection[required]"=if_supported \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  -d "tax_id_collection[required]"=if_supported \
  --mode=payment \
  --success-url="https://example.com/success"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {name: 'T-shirt'},
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {
    enabled: true,
    required: 'if_supported',
  },
  mode: 'payment',
  success_url: 'https://example.com/success',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "line_items": [
    {
      "price_data": {
        "unit_amount": 1000,
        "product_data": {"name": "T-shirt"},
        "currency": "eur",
      },
      "quantity": 2,
    },
  ],
  "tax_id_collection": {"enabled": True, "required": "if_supported"},
  "mode": "payment",
  "success_url": "https://example.com/success",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'line_items' => [
    [
      'price_data' => [
        'unit_amount' => 1000,
        'product_data' => ['name' => 'T-shirt'],
        'currency' => 'eur',
      ],
      'quantity' => 2,
    ],
  ],
  'tax_id_collection' => [
    'enabled' => true,
    'required' => 'if_supported',
  ],
  'mode' => 'payment',
  'success_url' => 'https://example.com/success',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setUnitAmount(1000L)
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setCurrency("eur")
            .build()
        )
        .setQuantity(2L)
        .build()
    )
    .setTaxIdCollection(
      SessionCreateParams.TaxIdCollection.builder().setEnabled(true).build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setSuccessUrl("https://example.com/success")
    .putExtraParam("tax_id_collection[required]", "if_supported")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {
          name: 'T-shirt',
        },
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {
    enabled: true,
    required: 'if_supported',
  },
  mode: 'payment',
  success_url: 'https://example.com/success',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        UnitAmount: stripe.Int64(1000),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        Currency: stripe.String(stripe.CurrencyEUR),
      },
      Quantity: stripe.Int64(2),
    },
  },
  TaxIDCollection: &stripe.CheckoutSessionCreateTaxIDCollectionParams{
    Enabled: stripe.Bool(true),
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  SuccessURL: stripe.String("https://example.com/success"),
}
params.AddExtra("tax_id_collection[required]", "if_supported")
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                UnitAmount = 1000,
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                Currency = "eur",
            },
            Quantity = 2,
        },
    },
    TaxIdCollection = new Stripe.Checkout.SessionTaxIdCollectionOptions
    {
        Enabled = true,
    },
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
};
options.AddExtraParam("tax_id_collection[required]", "if_supported");
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

## Retrieve Customer Tax ID details after a Session

Checkout includes provided tax IDs on the resulting [Session](https://docs.stripe.com/api/checkout/sessions/object.md) object. After each completed Session, Checkout emits a [checkout.session.completed](https://docs.stripe.com/api/events/types.md#event_types-checkout.session.completed) event that you can listen for in a *webhook* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests) endpoint. If you want to retrieve the collected tax ID from the Session object, it’s available under the Session’s [customer_details.tax_ids](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-customer_details-tax_ids) array:

```json
{
  "object": {
    "id": "cs_test_a1dJwt0TCJTBsDkbK7RcoyJ91vJxe2Y",
    "object": "checkout.session",
    ...
    "customer": "cus_id_of_new_customer",
    "customer_details": {
      ..."tax_ids": [
        {
          "type": "eu_vat",
          "value": "FRAB123456789"
        }
      ]
    },
    ..."tax_id_collection": {
      "enabled": true
    },
    ...
  }
}
```

Checkout also saves collected tax IDs and business names to the [Customer](https://docs.stripe.com/api/customers/object.md) object if one is associated with the Session. A tax ID collected during checkout is accessible under the Customer’s [customer.tax_ids](https://docs.stripe.com/api/customers/object.md#customer_object-tax_ids) array. You can also retrieve all tax IDs saved to a Customer with the [Tax IDs](https://docs.stripe.com/api/tax_ids/list.md) resource by specifying the [owner.type](https://docs.stripe.com/api/tax_ids/list.md#list_tax_ids-owner-type) parameter to `customer` and [owner.customer](https://docs.stripe.com/api/tax_ids/list.md#list_tax_ids-owner-customer) to the Customer’s ID. Every new tax ID includes an associated legal business name, which Checkout saves to the Customer’s [name](https://docs.stripe.com/api/customers/object.md#customer_object-name) property. In doing so, the collected legal business name is always visible on any subscription invoices for that Customer.

## Test your integration

In testing environments, you can enter any alphanumeric string that is in the correct format of a supported tax ID type (for example, `DE123456789` for `eu_vat`). For a full list of example tax IDs you can reference our [Customer Tax ID guide](https://docs.stripe.com/billing/customer/tax-ids.md#supported-tax-id). You can also use our [test tax IDs](https://docs.stripe.com/connect/testing.md#test-business-tax-ids) to test various verification state flows.

## Validation 

During the Checkout Session, Stripe verifies that the provided tax IDs are formatted correctly, but not that they’re valid. You’re responsible for ensuring the validity of customer information collected during checkout. To help, Stripe automatically performs asynchronous validation against government databases for [European Value Added Tax (EU VAT)](https://docs.stripe.com/billing/customer/tax-ids.md#eu-vat) and [United Kingdom Value Added Tax (GB VAT)](https://docs.stripe.com/billing/customer/tax-ids.md#gb-vat) numbers. Learn more about the [validation we perform](https://docs.stripe.com/tax/invoicing/tax-ids.md#validation), and how to consume the status of those checks.

If you use Stripe Tax and your customer provides a tax ID, Stripe Tax applies the reverse charge or zero rate according to applicable laws, as long as the tax ID conforms to the necessary number format, regardless of its validity.

## Supported Tax ID types 

Checkout collects the following tax ID types in the given regions:

| Country | Enum       | Description                                                                 | Example              | Impact in Tax Calculation* |
| ------- | ---------- | --------------------------------------------------------------------------- | -------------------- | -------------------------- |
| AE      | ae_trn     | United Arab Emirates TRN                                                    | 123456789012345      | Yes                        |
| AL      | al_tin     | Albania Tax Identification Number                                           | J12345678N           | Yes                        |
| AM      | am_tin     | Armenia Tax Identification Number                                           | 02538904             | Yes                        |
| AO      | ao_tin     | Angola Tax Identification Number                                            | 5123456789           | No                         |
| AT      | eu_vat     | European VAT number                                                         | ATU12345678          | Yes                        |
| AU      | au_abn     | Australian Business Number (AU ABN)                                         | 12345678912          | Yes                        |
| AW      | aw_tin     | Aruba Tax Identification Number                                             | 12345678             | Yes                        |
| AZ      | az_tin     | Azerbaijan Tax Identification Number                                        | 0123456789           | Yes                        |
| BA      | ba_tin     | Bosnia and Herzegovina Tax Identification Number                            | 123456789012         | Yes                        |
| BB      | bb_tin     | Barbados Tax Identification Number                                          | 1123456789012        | No                         |
| BD      | bd_bin     | Bangladesh Business Identification Number                                   | 123456789-0123       | Yes                        |
| BE      | eu_vat     | European VAT number                                                         | BE0123456789         | Yes                        |
| BF      | bf_ifu     | Burkina Faso Tax Identification Number (Numéro d'Identifiant Fiscal Unique) | 12345678A            | Yes                        |
| BG      | eu_vat     | European VAT number                                                         | BG0123456789         | Yes                        |
| BH      | bh_vat     | Bahraini VAT Number                                                         | 123456789012345      | Yes                        |
| BJ      | bj_ifu     | Benin Tax Identification Number (Identifiant Fiscal Unique)                 | 1234567890123        | Yes                        |
| BS      | bs_tin     | Bahamas Tax Identification Number                                           | 123.456.789          | No                         |
| BY      | by_tin     | Belarus TIN Number                                                          | 123456789            | Yes                        |
| CA      | ca_bn      | Canadian BN                                                                 | 123456789            | No                         |
| CA      | ca_gst_hst | Canadian GST/HST number                                                     | 123456789RT0002      | Yes                        |
| CA      | ca_pst_bc  | Canadian PST number (British Columbia)                                      | PST-1234-5678        | No                         |
| CA      | ca_pst_mb  | Canadian PST number (Manitoba)                                              | 123456-7             | No                         |
| CA      | ca_pst_sk  | Canadian PST number (Saskatchewan)                                          | 1234567              | No                         |
| CA      | ca_qst     | Canadian QST number (Québec)                                                | 1234567890TQ1234     | Yes                        |
| CD      | cd_nif     | Congo (DR) Tax Identification Number (Número de Identificação Fiscal)       | A0123456M            | No                         |
| CH      | ch_vat     | Switzerland VAT number                                                      | CHE-123.456.789 MWST | Yes                        |
| CL      | cl_tin     | Chilean TIN                                                                 | 12.345.678-K         | Yes                        |
| CM      | cm_niu     | Cameroon Tax Identification Number (Numéro d'Identifiant fiscal Unique)     | M123456789000L       | No                         |
| CR      | cr_tin     | Costa Rican tax ID                                                          | 1-234-567890         | No                         |
| CV      | cv_nif     | Cape Verde Tax Identification Number (Número de Identificação Fiscal)       | 213456789            | No                         |
| CY      | eu_vat     | European VAT number                                                         | CY12345678Z          | Yes                        |
| CZ      | eu_vat     | European VAT number                                                         | CZ1234567890         | Yes                        |
| DE      | eu_vat     | European VAT number                                                         | DE123456789          | Yes                        |
| DK      | eu_vat     | European VAT number                                                         | DK12345678           | Yes                        |
| EC      | ec_ruc     | Ecuadorian RUC number                                                       | 1234567890001        | No                         |
| EE      | eu_vat     | European VAT number                                                         | EE123456789          | Yes                        |
| EG      | eg_tin     | Egyptian Tax Identification Number                                          | 123456789            | Yes                        |
| ES      | es_cif     | Spanish NIF number (previously Spanish CIF number)                          | A12345678            | No                         |
| ES      | eu_vat     | European VAT number                                                         | ESA1234567Z          | Yes                        |
| ET      | et_tin     | Ethiopia Tax Identification Number                                          | 1234567890           | Yes                        |
| FI      | eu_vat     | European VAT number                                                         | FI12345678           | Yes                        |
| FR      | eu_vat     | European VAT number                                                         | FRAB123456789        | Yes                        |
| GB      | eu_vat     | Northern Ireland VAT number                                                 | XI123456789          | Yes                        |
| GB      | gb_vat     | United Kingdom VAT number                                                   | GB123456789          | Yes                        |
| GE      | ge_vat     | Georgian VAT                                                                | 123456789            | Yes                        |
| GN      | gn_nif     | Guinea Tax Identification Number (Número de Identificação Fiscal)           | 123456789            | Yes                        |
| GR      | eu_vat     | European VAT number                                                         | EL123456789          | Yes                        |
| HR      | eu_vat     | European VAT number                                                         | HR12345678912        | Yes                        |
| HU      | eu_vat     | European VAT number                                                         | HU12345678           | Yes                        |
| IE      | eu_vat     | European VAT number                                                         | IE1234567AB          | Yes                        |
| IN      | in_gst     | Indian GST number                                                           | 12ABCDE3456FGZH      | Yes                        |
| IS      | is_vat     | Icelandic VAT                                                               | 123456               | Yes                        |
| IT      | eu_vat     | European VAT number                                                         | IT12345678912        | Yes                        |
| KE      | ke_pin     | Kenya Revenue Authority Personal Identification Number                      | P000111111A          | No                         |
| KG      | kg_tin     | Kyrgyzstan Tax Identification Number                                        | 12345678901234       | No                         |
| KH      | kh_tin     | Cambodia Tax Identification Number                                          | 1001-123456789       | Yes                        |
| KR      | kr_brn     | Korean BRN                                                                  | 123-45-67890         | Yes                        |
| KZ      | kz_bin     | Kazakhstani Business Identification Number                                  | 123456789012         | Yes                        |
| LA      | la_tin     | Laos Tax Identification Number                                              | 123456789-000        | No                         |
| LI      | li_vat     | Liechtensteinian VAT number                                                 | 12345                | Yes                        |
| LT      | eu_vat     | European VAT number                                                         | LT123456789123       | Yes                        |
| LU      | eu_vat     | European VAT number                                                         | LU12345678           | Yes                        |
| LV      | eu_vat     | European VAT number                                                         | LV12345678912        | Yes                        |
| MA      | ma_vat     | Morocco VAT Number                                                          | 12345678             | Yes                        |
| MD      | md_vat     | Moldova VAT Number                                                          | 1234567              | Yes                        |
| ME      | me_pib     | Montenegro PIB Number                                                       | 12345678             | No                         |
| MK      | mk_vat     | North Macedonia VAT Number                                                  | MK1234567890123      | Yes                        |
| MR      | mr_nif     | Mauritania Tax Identification Number (Número de Identificação Fiscal)       | 12345678             | No                         |
| MT      | eu_vat     | European VAT number                                                         | MT12345678           | Yes                        |
| MX      | mx_rfc     | Mexican RFC number                                                          | ABC010203AB9         | No                         |
| NG      | ng_tin     | Nigerian Tax Identification Number                                          | 12345678-0001        | No                         |
| NL      | eu_vat     | European VAT number                                                         | NL123456789B12       | Yes                        |
| NO      | no_vat     | Norwegian VAT number                                                        | 123456789MVA         | Yes                        |
| NP      | np_pan     | Nepal PAN Number                                                            | 123456789            | Yes                        |
| NZ      | nz_gst     | New Zealand GST number                                                      | 123456789            | Yes                        |
| OM      | om_vat     | Omani VAT Number                                                            | OM1234567890         | Yes                        |
| PE      | pe_ruc     | Peruvian RUC number                                                         | 12345678901          | Yes                        |
| PH      | ph_tin     | Philippines Tax Identification Number                                       | 123456789012         | Yes                        |
| PL      | eu_vat     | European VAT number                                                         | PL1234567890         | Yes                        |
| PT      | eu_vat     | European VAT number                                                         | PT123456789          | Yes                        |
| RO      | eu_vat     | European VAT number                                                         | RO1234567891         | Yes                        |
| RS      | rs_pib     | Serbian PIB number                                                          | 123456789            | No                         |
| RU      | ru_inn     | Russian INN                                                                 | 1234567891           | Yes                        |
| RU      | ru_kpp     | Russian KPP                                                                 | 123456789            | Yes                        |
| SA      | sa_vat     | Saudi Arabia VAT                                                            | 123456789012345      | Yes                        |
| SE      | eu_vat     | European VAT number                                                         | SE123456789123       | Yes                        |
| SG      | sg_gst     | Singaporean GST                                                             | M12345678X           | Yes                        |
| SI      | eu_vat     | European VAT number                                                         | SI12345678           | Yes                        |
| SK      | eu_vat     | European VAT number                                                         | SK1234567891         | Yes                        |
| SN      | sn_ninea   | Senegal NINEA Number                                                        | 12345672A2           | No                         |
| SR      | sr_fin     | Suriname FIN Number                                                         | 1234567890           | Yes                        |
| TH      | th_vat     | Thai VAT                                                                    | 1234567891234        | Yes                        |
| TJ      | tj_tin     | Tajikistan Tax Identification Number                                        | 123456789            | Yes                        |
| TR      | tr_tin     | Turkish Tax Identification Number                                           | 0123456789           | Yes                        |
| TW      | tw_vat     | Taiwanese VAT                                                               | 12345678             | Yes                        |
| TZ      | tz_vat     | Tanzania VAT Number                                                         | 12345678A            | Yes                        |
| UA      | ua_vat     | Ukrainian VAT                                                               | 123456789            | Yes                        |
| UG      | ug_tin     | Uganda Tax Identification Number                                            | 1014751879           | Yes                        |
| UY      | uy_ruc     | Uruguayan RUC number                                                        | 123456789012         | Yes                        |
| UZ      | uz_tin     | Uzbekistan TIN Number                                                       | 123456789            | No                         |
| UZ      | uz_vat     | Uzbekistan VAT Number                                                       | 123456789012         | Yes                        |
| ZA      | za_vat     | South African VAT number                                                    | 4123456789           | Yes                        |
| ZM      | zm_tin     | Zambia Tax Identification Number                                            | 1004751879           | No                         |
| ZW      | zw_tin     | Zimbabwe Tax Identification Number                                          | 1234567890           | No                         |

\*Stripe Tax won't apply tax if this tax ID is provided, in line with the relevant laws.


# Embedded form

> This is a Embedded form for when payment-ui is embedded-form. View the full page at https://docs.stripe.com/tax/checkout/tax-ids?payment-ui=embedded-form.

Displaying a customer’s tax ID and legal business name on *invoices* (Invoices are statements of amounts owed by a customer. They track the status of payments from draft through paid or otherwise finalized. Subscriptions automatically generate invoices, or you can manually create a one-off invoice) is a common requirement that you can satisfy by enabling tax ID collection in Checkout. This guide assumes that you’ve already integrated Checkout. If you haven’t, see the [Accept a payment guide](https://docs.stripe.com/payments/accept-a-payment.md).

## Enable Tax ID collection

With tax ID collection enabled, Checkout shows and hides the tax ID collection form depending on your customer’s location. If your customer is in a location supported by tax ID collection, Checkout shows a checkbox allowing the customer to indicate that they’re purchasing as a business. When a customer checks the box, Checkout displays fields for them to enter the tax ID and legal entity name for the business. If available, Checkout uses the customer’s shipping address to determine their location, otherwise Checkout uses the customer’s billing address. Customers can only enter one tax ID.

### New Customers

To enable tax ID collection for new customers, set [tax_id_collection[enabled]](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-tax_id_collection-enabled) to `true` when creating a Checkout session.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/return"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {name: 'T-shirt'},
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {enabled: true},
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "line_items": [
    {
      "price_data": {
        "unit_amount": 1000,
        "product_data": {"name": "T-shirt"},
        "currency": "eur",
      },
      "quantity": 2,
    },
  ],
  "tax_id_collection": {"enabled": True},
  "mode": "payment",
  "ui_mode": "embedded",
  "return_url": "https://example.com/return",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'line_items' => [
    [
      'price_data' => [
        'unit_amount' => 1000,
        'product_data' => ['name' => 'T-shirt'],
        'currency' => 'eur',
      ],
      'quantity' => 2,
    ],
  ],
  'tax_id_collection' => ['enabled' => true],
  'mode' => 'payment',
  'ui_mode' => 'embedded',
  'return_url' => 'https://example.com/return',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setUnitAmount(1000L)
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setCurrency("eur")
            .build()
        )
        .setQuantity(2L)
        .build()
    )
    .setTaxIdCollection(
      SessionCreateParams.TaxIdCollection.builder().setEnabled(true).build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setReturnUrl("https://example.com/return")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {
          name: 'T-shirt',
        },
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {
    enabled: true,
  },
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        UnitAmount: stripe.Int64(1000),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        Currency: stripe.String(stripe.CurrencyEUR),
      },
      Quantity: stripe.Int64(2),
    },
  },
  TaxIDCollection: &stripe.CheckoutSessionCreateTaxIDCollectionParams{
    Enabled: stripe.Bool(true),
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  ReturnURL: stripe.String("https://example.com/return"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                UnitAmount = 1000,
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                Currency = "eur",
            },
            Quantity = 2,
        },
    },
    TaxIdCollection = new Stripe.Checkout.SessionTaxIdCollectionOptions
    {
        Enabled = true,
    },
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

This example creates a Session in `payment` mode with tax ID collection enabled. For subscriptions, make the same changes with the [mode](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-mode) set to `subscription`.

You can additionally configure Checkout to create a new [Customer](https://docs.stripe.com/api/customers/object.md) for you using [customer_creation](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-customer_creation). If you do, Checkout saves any tax ID information collected during a Session to that new *Customer* (Customer objects represent customers of your business. They let you reuse payment methods and give you the ability to track multiple payments). If not, the tax ID information will still be available at [customer_details.tax_ids](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-customer_details-tax_ids).

### Existing Customers

If you pass an existing Customer when creating a Session, Checkout updates the Customer with any tax ID information collected during the Session. Checkout saves the collected business name onto the Customer’s [name](https://docs.stripe.com/api/customers/object.md#customer_object-name) property, and adds the collected tax ID to the Customer’s [customer.tax_ids](https://docs.stripe.com/api/customers/object.md#customer_object-tax_ids) array. Since the collection of a business name could result in the Customer’s existing [name](https://docs.stripe.com/api/customers/object.md#customer_object-name) being overridden, you must set [customer_update.name](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-customer_update-name) to `auto` when creating the Session.

> Checkout only collects tax IDs on Customers that don’t already have an existing tax ID. If a Customer has one or more tax IDs saved, Checkout doesn’t display the tax ID collection form even if tax ID collection is enabled.

When collecting tax IDs for existing customers you can either base their location on existing addresses on the customer or the addresses entered during checkout. By default, Checkout looks for existing addresses on the customer to assess their location:

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  -d "customer_update[name]"=auto \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  -d "customer_update[name]"=auto \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/return"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  customer: '{{CUSTOMER_ID}}',
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {name: 'T-shirt'},
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {enabled: true},
  customer_update: {name: 'auto'},
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "customer": "{{CUSTOMER_ID}}",
  "line_items": [
    {
      "price_data": {
        "unit_amount": 1000,
        "product_data": {"name": "T-shirt"},
        "currency": "eur",
      },
      "quantity": 2,
    },
  ],
  "tax_id_collection": {"enabled": True},
  "customer_update": {"name": "auto"},
  "mode": "payment",
  "ui_mode": "embedded",
  "return_url": "https://example.com/return",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'customer' => '{{CUSTOMER_ID}}',
  'line_items' => [
    [
      'price_data' => [
        'unit_amount' => 1000,
        'product_data' => ['name' => 'T-shirt'],
        'currency' => 'eur',
      ],
      'quantity' => 2,
    ],
  ],
  'tax_id_collection' => ['enabled' => true],
  'customer_update' => ['name' => 'auto'],
  'mode' => 'payment',
  'ui_mode' => 'embedded',
  'return_url' => 'https://example.com/return',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setUnitAmount(1000L)
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setCurrency("eur")
            .build()
        )
        .setQuantity(2L)
        .build()
    )
    .setTaxIdCollection(
      SessionCreateParams.TaxIdCollection.builder().setEnabled(true).build()
    )
    .setCustomerUpdate(
      SessionCreateParams.CustomerUpdate.builder()
        .setName(SessionCreateParams.CustomerUpdate.Name.AUTO)
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setReturnUrl("https://example.com/return")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  customer: '{{CUSTOMER_ID}}',
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {
          name: 'T-shirt',
        },
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {
    enabled: true,
  },
  customer_update: {
    name: 'auto',
  },
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        UnitAmount: stripe.Int64(1000),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        Currency: stripe.String(stripe.CurrencyEUR),
      },
      Quantity: stripe.Int64(2),
    },
  },
  TaxIDCollection: &stripe.CheckoutSessionCreateTaxIDCollectionParams{
    Enabled: stripe.Bool(true),
  },
  CustomerUpdate: &stripe.CheckoutSessionCreateCustomerUpdateParams{
    Name: stripe.String("auto"),
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  ReturnURL: stripe.String("https://example.com/return"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                UnitAmount = 1000,
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                Currency = "eur",
            },
            Quantity = 2,
        },
    },
    TaxIdCollection = new Stripe.Checkout.SessionTaxIdCollectionOptions
    {
        Enabled = true,
    },
    CustomerUpdate = new Stripe.Checkout.SessionCustomerUpdateOptions { Name = "auto" },
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

If you don’t have the addresses of your existing customers saved, you can base their location on the billing or shipping address entered during Checkout. To specify that you want to use the billing address entered during Checkout to assess the customer’s location, you must set [customer_update.address](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-customer_update-address) to `auto`. When setting [customer_update.address](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-customer_update-address) to `auto`, Checkout replaces any previously saved addresses on the customer with the address entered during the session.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  -d "customer_update[name]"=auto \
  -d "customer_update[address]"=auto \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  -d "customer_update[name]"=auto \
  -d "customer_update[address]"=auto \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/return"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  customer: '{{CUSTOMER_ID}}',
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {name: 'T-shirt'},
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {enabled: true},
  customer_update: {
    name: 'auto',
    address: 'auto',
  },
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "customer": "{{CUSTOMER_ID}}",
  "line_items": [
    {
      "price_data": {
        "unit_amount": 1000,
        "product_data": {"name": "T-shirt"},
        "currency": "eur",
      },
      "quantity": 2,
    },
  ],
  "tax_id_collection": {"enabled": True},
  "customer_update": {"name": "auto", "address": "auto"},
  "mode": "payment",
  "ui_mode": "embedded",
  "return_url": "https://example.com/return",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'customer' => '{{CUSTOMER_ID}}',
  'line_items' => [
    [
      'price_data' => [
        'unit_amount' => 1000,
        'product_data' => ['name' => 'T-shirt'],
        'currency' => 'eur',
      ],
      'quantity' => 2,
    ],
  ],
  'tax_id_collection' => ['enabled' => true],
  'customer_update' => [
    'name' => 'auto',
    'address' => 'auto',
  ],
  'mode' => 'payment',
  'ui_mode' => 'embedded',
  'return_url' => 'https://example.com/return',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setUnitAmount(1000L)
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setCurrency("eur")
            .build()
        )
        .setQuantity(2L)
        .build()
    )
    .setTaxIdCollection(
      SessionCreateParams.TaxIdCollection.builder().setEnabled(true).build()
    )
    .setCustomerUpdate(
      SessionCreateParams.CustomerUpdate.builder()
        .setName(SessionCreateParams.CustomerUpdate.Name.AUTO)
        .setAddress(SessionCreateParams.CustomerUpdate.Address.AUTO)
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setReturnUrl("https://example.com/return")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  customer: '{{CUSTOMER_ID}}',
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {
          name: 'T-shirt',
        },
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {
    enabled: true,
  },
  customer_update: {
    name: 'auto',
    address: 'auto',
  },
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        UnitAmount: stripe.Int64(1000),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        Currency: stripe.String(stripe.CurrencyEUR),
      },
      Quantity: stripe.Int64(2),
    },
  },
  TaxIDCollection: &stripe.CheckoutSessionCreateTaxIDCollectionParams{
    Enabled: stripe.Bool(true),
  },
  CustomerUpdate: &stripe.CheckoutSessionCreateCustomerUpdateParams{
    Name: stripe.String("auto"),
    Address: stripe.String("auto"),
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  ReturnURL: stripe.String("https://example.com/return"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                UnitAmount = 1000,
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                Currency = "eur",
            },
            Quantity = 2,
        },
    },
    TaxIdCollection = new Stripe.Checkout.SessionTaxIdCollectionOptions
    {
        Enabled = true,
    },
    CustomerUpdate = new Stripe.Checkout.SessionCustomerUpdateOptions
    {
        Name = "auto",
        Address = "auto",
    },
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

If you’re collecting shipping addresses for existing customers, you must base their location on the shipping address entered during checkout. To do so, set [customer_update.shipping](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-customer_update-shipping) to `auto`. When setting [customer_update.shipping](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-customer_update-shipping) to `auto`, Checkout replaces any previously saved shipping addresses on the customer with the shipping address entered during the session.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer=cus_HQmikpKnGHkNwW \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  -d "customer_update[name]"=auto \
  -d "customer_update[shipping]"=auto \
  -d "shipping_address_collection[allowed_countries][0]"=DE \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  --customer=cus_HQmikpKnGHkNwW \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  -d "customer_update[name]"=auto \
  -d "customer_update[shipping]"=auto \
  -d "shipping_address_collection[allowed_countries][0]"=DE \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/return"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  customer: 'cus_HQmikpKnGHkNwW',
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {name: 'T-shirt'},
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {enabled: true},
  customer_update: {
    name: 'auto',
    shipping: 'auto',
  },
  shipping_address_collection: {allowed_countries: ['DE']},
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "customer": "cus_HQmikpKnGHkNwW",
  "line_items": [
    {
      "price_data": {
        "unit_amount": 1000,
        "product_data": {"name": "T-shirt"},
        "currency": "eur",
      },
      "quantity": 2,
    },
  ],
  "tax_id_collection": {"enabled": True},
  "customer_update": {"name": "auto", "shipping": "auto"},
  "shipping_address_collection": {"allowed_countries": ["DE"]},
  "mode": "payment",
  "ui_mode": "embedded",
  "return_url": "https://example.com/return",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'customer' => 'cus_HQmikpKnGHkNwW',
  'line_items' => [
    [
      'price_data' => [
        'unit_amount' => 1000,
        'product_data' => ['name' => 'T-shirt'],
        'currency' => 'eur',
      ],
      'quantity' => 2,
    ],
  ],
  'tax_id_collection' => ['enabled' => true],
  'customer_update' => [
    'name' => 'auto',
    'shipping' => 'auto',
  ],
  'shipping_address_collection' => ['allowed_countries' => ['DE']],
  'mode' => 'payment',
  'ui_mode' => 'embedded',
  'return_url' => 'https://example.com/return',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .setCustomer("cus_HQmikpKnGHkNwW")
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setUnitAmount(1000L)
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setCurrency("eur")
            .build()
        )
        .setQuantity(2L)
        .build()
    )
    .setTaxIdCollection(
      SessionCreateParams.TaxIdCollection.builder().setEnabled(true).build()
    )
    .setCustomerUpdate(
      SessionCreateParams.CustomerUpdate.builder()
        .setName(SessionCreateParams.CustomerUpdate.Name.AUTO)
        .setShipping(SessionCreateParams.CustomerUpdate.Shipping.AUTO)
        .build()
    )
    .setShippingAddressCollection(
      SessionCreateParams.ShippingAddressCollection.builder()
        .addAllowedCountry(
          SessionCreateParams.ShippingAddressCollection.AllowedCountry.DE
        )
        .build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setReturnUrl("https://example.com/return")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  customer: 'cus_HQmikpKnGHkNwW',
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {
          name: 'T-shirt',
        },
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {
    enabled: true,
  },
  customer_update: {
    name: 'auto',
    shipping: 'auto',
  },
  shipping_address_collection: {
    allowed_countries: ['DE'],
  },
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  Customer: stripe.String("cus_HQmikpKnGHkNwW"),
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        UnitAmount: stripe.Int64(1000),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        Currency: stripe.String(stripe.CurrencyEUR),
      },
      Quantity: stripe.Int64(2),
    },
  },
  TaxIDCollection: &stripe.CheckoutSessionCreateTaxIDCollectionParams{
    Enabled: stripe.Bool(true),
  },
  CustomerUpdate: &stripe.CheckoutSessionCreateCustomerUpdateParams{
    Name: stripe.String("auto"),
    Shipping: stripe.String("auto"),
  },
  ShippingAddressCollection: &stripe.CheckoutSessionCreateShippingAddressCollectionParams{
    AllowedCountries: []*string{stripe.String("DE")},
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  ReturnURL: stripe.String("https://example.com/return"),
}
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    Customer = "cus_HQmikpKnGHkNwW",
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                UnitAmount = 1000,
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                Currency = "eur",
            },
            Quantity = 2,
        },
    },
    TaxIdCollection = new Stripe.Checkout.SessionTaxIdCollectionOptions
    {
        Enabled = true,
    },
    CustomerUpdate = new Stripe.Checkout.SessionCustomerUpdateOptions
    {
        Name = "auto",
        Shipping = "auto",
    },
    ShippingAddressCollection = new Stripe.Checkout.SessionShippingAddressCollectionOptions
    {
        AllowedCountries = new List<string> { "DE" },
    },
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

This example creates a Session in `payment` mode with tax ID collection enabled. For subscriptions, make the same changes with the [mode](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-mode) set to `subscription`.

## Optional: Require tax ID collection

You can optionally configure Checkout to require tax ID collection by setting the [tax_id_collection[required]](https://docs.stripe.com/api/.md#create_checkout_session-tax_id_collection-required) parameter. When set to `if_supported`, Checkout will require tax ID information for payment for customers in [supported billing countries](https://docs.stripe.com/tax/checkout/tax-ids.md#supported-types).

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  -d "tax_id_collection[required]"=if_supported \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price_data][unit_amount]"=1000 \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][currency]"=eur \
  -d "line_items[0][quantity]"=2 \
  -d "tax_id_collection[enabled]"=true \
  -d "tax_id_collection[required]"=if_supported \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/return"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {name: 'T-shirt'},
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {
    enabled: true,
    required: 'if_supported',
  },
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
session = client.v1.checkout.sessions.create({
  "line_items": [
    {
      "price_data": {
        "unit_amount": 1000,
        "product_data": {"name": "T-shirt"},
        "currency": "eur",
      },
      "quantity": 2,
    },
  ],
  "tax_id_collection": {"enabled": True, "required": "if_supported"},
  "mode": "payment",
  "ui_mode": "embedded",
  "return_url": "https://example.com/return",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'line_items' => [
    [
      'price_data' => [
        'unit_amount' => 1000,
        'product_data' => ['name' => 'T-shirt'],
        'currency' => 'eur',
      ],
      'quantity' => 2,
    ],
  ],
  'tax_id_collection' => [
    'enabled' => true,
    'required' => 'if_supported',
  ],
  'mode' => 'payment',
  'ui_mode' => 'embedded',
  'return_url' => 'https://example.com/return',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SessionCreateParams params =
  SessionCreateParams.builder()
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setUnitAmount(1000L)
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setCurrency("eur")
            .build()
        )
        .setQuantity(2L)
        .build()
    )
    .setTaxIdCollection(
      SessionCreateParams.TaxIdCollection.builder().setEnabled(true).build()
    )
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
    .setReturnUrl("https://example.com/return")
    .putExtraParam("tax_id_collection[required]", "if_supported")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Session session = client.v1().checkout().sessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  line_items: [
    {
      price_data: {
        unit_amount: 1000,
        product_data: {
          name: 'T-shirt',
        },
        currency: 'eur',
      },
      quantity: 2,
    },
  ],
  tax_id_collection: {
    enabled: true,
    required: 'if_supported',
  },
  mode: 'payment',
  ui_mode: 'embedded',
  return_url: 'https://example.com/return',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        UnitAmount: stripe.Int64(1000),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        Currency: stripe.String(stripe.CurrencyEUR),
      },
      Quantity: stripe.Int64(2),
    },
  },
  TaxIDCollection: &stripe.CheckoutSessionCreateTaxIDCollectionParams{
    Enabled: stripe.Bool(true),
  },
  Mode: stripe.String(stripe.CheckoutSessionModePayment),
  UIMode: stripe.String(stripe.CheckoutSessionUIModeEmbedded),
  ReturnURL: stripe.String("https://example.com/return"),
}
params.AddExtra("tax_id_collection[required]", "if_supported")
result, err := sc.V1CheckoutSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Checkout.SessionCreateOptions
{
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                UnitAmount = 1000,
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                Currency = "eur",
            },
            Quantity = 2,
        },
    },
    TaxIdCollection = new Stripe.Checkout.SessionTaxIdCollectionOptions
    {
        Enabled = true,
    },
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
};
options.AddExtraParam("tax_id_collection[required]", "if_supported");
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

## Retrieve Customer Tax ID details after a Session

Checkout includes provided tax IDs on the resulting [Session](https://docs.stripe.com/api/checkout/sessions/object.md) object. After each completed Session, Checkout emits a [checkout.session.completed](https://docs.stripe.com/api/events/types.md#event_types-checkout.session.completed) event that you can listen for in a *webhook* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests) endpoint. If you want to retrieve the collected tax ID from the Session object, it’s available under the Session’s [customer_details.tax_ids](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-customer_details-tax_ids) array:

```json
{
  "object": {
    "id": "cs_test_a1dJwt0TCJTBsDkbK7RcoyJ91vJxe2Y",
    "object": "checkout.session",
    ...
    "customer": "cus_id_of_new_customer",
    "customer_details": {
      ..."tax_ids": [
        {
          "type": "eu_vat",
          "value": "FRAB123456789"
        }
      ]
    },
    ..."tax_id_collection": {
      "enabled": true
    },
    ...
  }
}
```

Checkout also saves collected tax IDs and business names to the [Customer](https://docs.stripe.com/api/customers/object.md) object if one is associated with the Session. A tax ID collected during checkout is accessible under the Customer’s [customer.tax_ids](https://docs.stripe.com/api/customers/object.md#customer_object-tax_ids) array. You can also retrieve all tax IDs saved to a Customer with the [Tax IDs](https://docs.stripe.com/api/tax_ids/list.md) resource by specifying the [owner.type](https://docs.stripe.com/api/tax_ids/list.md#list_tax_ids-owner-type) parameter to `customer` and [owner.customer](https://docs.stripe.com/api/tax_ids/list.md#list_tax_ids-owner-customer) to the Customer’s ID. Every new tax ID includes an associated legal business name, which Checkout saves to the Customer’s [name](https://docs.stripe.com/api/customers/object.md#customer_object-name) property. In doing so, the collected legal business name is always visible on any subscription invoices for that Customer.

## Test your integration

In testing environments, you can enter any alphanumeric string that is in the correct format of a supported tax ID type (for example, `DE123456789` for `eu_vat`). For a full list of example tax IDs you can reference our [Customer Tax ID guide](https://docs.stripe.com/billing/customer/tax-ids.md#supported-tax-id). You can also use our [test tax IDs](https://docs.stripe.com/connect/testing.md#test-business-tax-ids) to test various verification state flows.

## Validation 

During the Checkout Session, Stripe verifies that the provided tax IDs are formatted correctly, but not that they’re valid. You’re responsible for ensuring the validity of customer information collected during checkout. To help, Stripe automatically performs asynchronous validation against government databases for [European Value Added Tax (EU VAT)](https://docs.stripe.com/billing/customer/tax-ids.md#eu-vat) and [United Kingdom Value Added Tax (GB VAT)](https://docs.stripe.com/billing/customer/tax-ids.md#gb-vat) numbers. Learn more about the [validation we perform](https://docs.stripe.com/tax/invoicing/tax-ids.md#validation), and how to consume the status of those checks.

If you use Stripe Tax and your customer provides a tax ID, Stripe Tax applies the reverse charge or zero rate according to applicable laws, as long as the tax ID conforms to the necessary number format, regardless of its validity.

## Supported Tax ID types 

Checkout collects the following tax ID types in the given regions:

| Country | Enum       | Description                                                                 | Example              | Impact in Tax Calculation* |
| ------- | ---------- | --------------------------------------------------------------------------- | -------------------- | -------------------------- |
| AE      | ae_trn     | United Arab Emirates TRN                                                    | 123456789012345      | Yes                        |
| AL      | al_tin     | Albania Tax Identification Number                                           | J12345678N           | Yes                        |
| AM      | am_tin     | Armenia Tax Identification Number                                           | 02538904             | Yes                        |
| AO      | ao_tin     | Angola Tax Identification Number                                            | 5123456789           | No                         |
| AT      | eu_vat     | European VAT number                                                         | ATU12345678          | Yes                        |
| AU      | au_abn     | Australian Business Number (AU ABN)                                         | 12345678912          | Yes                        |
| AW      | aw_tin     | Aruba Tax Identification Number                                             | 12345678             | Yes                        |
| AZ      | az_tin     | Azerbaijan Tax Identification Number                                        | 0123456789           | Yes                        |
| BA      | ba_tin     | Bosnia and Herzegovina Tax Identification Number                            | 123456789012         | Yes                        |
| BB      | bb_tin     | Barbados Tax Identification Number                                          | 1123456789012        | No                         |
| BD      | bd_bin     | Bangladesh Business Identification Number                                   | 123456789-0123       | Yes                        |
| BE      | eu_vat     | European VAT number                                                         | BE0123456789         | Yes                        |
| BF      | bf_ifu     | Burkina Faso Tax Identification Number (Numéro d'Identifiant Fiscal Unique) | 12345678A            | Yes                        |
| BG      | eu_vat     | European VAT number                                                         | BG0123456789         | Yes                        |
| BH      | bh_vat     | Bahraini VAT Number                                                         | 123456789012345      | Yes                        |
| BJ      | bj_ifu     | Benin Tax Identification Number (Identifiant Fiscal Unique)                 | 1234567890123        | Yes                        |
| BS      | bs_tin     | Bahamas Tax Identification Number                                           | 123.456.789          | No                         |
| BY      | by_tin     | Belarus TIN Number                                                          | 123456789            | Yes                        |
| CA      | ca_bn      | Canadian BN                                                                 | 123456789            | No                         |
| CA      | ca_gst_hst | Canadian GST/HST number                                                     | 123456789RT0002      | Yes                        |
| CA      | ca_pst_bc  | Canadian PST number (British Columbia)                                      | PST-1234-5678        | No                         |
| CA      | ca_pst_mb  | Canadian PST number (Manitoba)                                              | 123456-7             | No                         |
| CA      | ca_pst_sk  | Canadian PST number (Saskatchewan)                                          | 1234567              | No                         |
| CA      | ca_qst     | Canadian QST number (Québec)                                                | 1234567890TQ1234     | Yes                        |
| CD      | cd_nif     | Congo (DR) Tax Identification Number (Número de Identificação Fiscal)       | A0123456M            | No                         |
| CH      | ch_vat     | Switzerland VAT number                                                      | CHE-123.456.789 MWST | Yes                        |
| CL      | cl_tin     | Chilean TIN                                                                 | 12.345.678-K         | Yes                        |
| CM      | cm_niu     | Cameroon Tax Identification Number (Numéro d'Identifiant fiscal Unique)     | M123456789000L       | No                         |
| CR      | cr_tin     | Costa Rican tax ID                                                          | 1-234-567890         | No                         |
| CV      | cv_nif     | Cape Verde Tax Identification Number (Número de Identificação Fiscal)       | 213456789            | No                         |
| CY      | eu_vat     | European VAT number                                                         | CY12345678Z          | Yes                        |
| CZ      | eu_vat     | European VAT number                                                         | CZ1234567890         | Yes                        |
| DE      | eu_vat     | European VAT number                                                         | DE123456789          | Yes                        |
| DK      | eu_vat     | European VAT number                                                         | DK12345678           | Yes                        |
| EC      | ec_ruc     | Ecuadorian RUC number                                                       | 1234567890001        | No                         |
| EE      | eu_vat     | European VAT number                                                         | EE123456789          | Yes                        |
| EG      | eg_tin     | Egyptian Tax Identification Number                                          | 123456789            | Yes                        |
| ES      | es_cif     | Spanish NIF number (previously Spanish CIF number)                          | A12345678            | No                         |
| ES      | eu_vat     | European VAT number                                                         | ESA1234567Z          | Yes                        |
| ET      | et_tin     | Ethiopia Tax Identification Number                                          | 1234567890           | Yes                        |
| FI      | eu_vat     | European VAT number                                                         | FI12345678           | Yes                        |
| FR      | eu_vat     | European VAT number                                                         | FRAB123456789        | Yes                        |
| GB      | eu_vat     | Northern Ireland VAT number                                                 | XI123456789          | Yes                        |
| GB      | gb_vat     | United Kingdom VAT number                                                   | GB123456789          | Yes                        |
| GE      | ge_vat     | Georgian VAT                                                                | 123456789            | Yes                        |
| GN      | gn_nif     | Guinea Tax Identification Number (Número de Identificação Fiscal)           | 123456789            | Yes                        |
| GR      | eu_vat     | European VAT number                                                         | EL123456789          | Yes                        |
| HR      | eu_vat     | European VAT number                                                         | HR12345678912        | Yes                        |
| HU      | eu_vat     | European VAT number                                                         | HU12345678           | Yes                        |
| IE      | eu_vat     | European VAT number                                                         | IE1234567AB          | Yes                        |
| IN      | in_gst     | Indian GST number                                                           | 12ABCDE3456FGZH      | Yes                        |
| IS      | is_vat     | Icelandic VAT                                                               | 123456               | Yes                        |
| IT      | eu_vat     | European VAT number                                                         | IT12345678912        | Yes                        |
| KE      | ke_pin     | Kenya Revenue Authority Personal Identification Number                      | P000111111A          | No                         |
| KG      | kg_tin     | Kyrgyzstan Tax Identification Number                                        | 12345678901234       | No                         |
| KH      | kh_tin     | Cambodia Tax Identification Number                                          | 1001-123456789       | Yes                        |
| KR      | kr_brn     | Korean BRN                                                                  | 123-45-67890         | Yes                        |
| KZ      | kz_bin     | Kazakhstani Business Identification Number                                  | 123456789012         | Yes                        |
| LA      | la_tin     | Laos Tax Identification Number                                              | 123456789-000        | No                         |
| LI      | li_vat     | Liechtensteinian VAT number                                                 | 12345                | Yes                        |
| LT      | eu_vat     | European VAT number                                                         | LT123456789123       | Yes                        |
| LU      | eu_vat     | European VAT number                                                         | LU12345678           | Yes                        |
| LV      | eu_vat     | European VAT number                                                         | LV12345678912        | Yes                        |
| MA      | ma_vat     | Morocco VAT Number                                                          | 12345678             | Yes                        |
| MD      | md_vat     | Moldova VAT Number                                                          | 1234567              | Yes                        |
| ME      | me_pib     | Montenegro PIB Number                                                       | 12345678             | No                         |
| MK      | mk_vat     | North Macedonia VAT Number                                                  | MK1234567890123      | Yes                        |
| MR      | mr_nif     | Mauritania Tax Identification Number (Número de Identificação Fiscal)       | 12345678             | No                         |
| MT      | eu_vat     | European VAT number                                                         | MT12345678           | Yes                        |
| MX      | mx_rfc     | Mexican RFC number                                                          | ABC010203AB9         | No                         |
| NG      | ng_tin     | Nigerian Tax Identification Number                                          | 12345678-0001        | No                         |
| NL      | eu_vat     | European VAT number                                                         | NL123456789B12       | Yes                        |
| NO      | no_vat     | Norwegian VAT number                                                        | 123456789MVA         | Yes                        |
| NP      | np_pan     | Nepal PAN Number                                                            | 123456789            | Yes                        |
| NZ      | nz_gst     | New Zealand GST number                                                      | 123456789            | Yes                        |
| OM      | om_vat     | Omani VAT Number                                                            | OM1234567890         | Yes                        |
| PE      | pe_ruc     | Peruvian RUC number                                                         | 12345678901          | Yes                        |
| PH      | ph_tin     | Philippines Tax Identification Number                                       | 123456789012         | Yes                        |
| PL      | eu_vat     | European VAT number                                                         | PL1234567890         | Yes                        |
| PT      | eu_vat     | European VAT number                                                         | PT123456789          | Yes                        |
| RO      | eu_vat     | European VAT number                                                         | RO1234567891         | Yes                        |
| RS      | rs_pib     | Serbian PIB number                                                          | 123456789            | No                         |
| RU      | ru_inn     | Russian INN                                                                 | 1234567891           | Yes                        |
| RU      | ru_kpp     | Russian KPP                                                                 | 123456789            | Yes                        |
| SA      | sa_vat     | Saudi Arabia VAT                                                            | 123456789012345      | Yes                        |
| SE      | eu_vat     | European VAT number                                                         | SE123456789123       | Yes                        |
| SG      | sg_gst     | Singaporean GST                                                             | M12345678X           | Yes                        |
| SI      | eu_vat     | European VAT number                                                         | SI12345678           | Yes                        |
| SK      | eu_vat     | European VAT number                                                         | SK1234567891         | Yes                        |
| SN      | sn_ninea   | Senegal NINEA Number                                                        | 12345672A2           | No                         |
| SR      | sr_fin     | Suriname FIN Number                                                         | 1234567890           | Yes                        |
| TH      | th_vat     | Thai VAT                                                                    | 1234567891234        | Yes                        |
| TJ      | tj_tin     | Tajikistan Tax Identification Number                                        | 123456789            | Yes                        |
| TR      | tr_tin     | Turkish Tax Identification Number                                           | 0123456789           | Yes                        |
| TW      | tw_vat     | Taiwanese VAT                                                               | 12345678             | Yes                        |
| TZ      | tz_vat     | Tanzania VAT Number                                                         | 12345678A            | Yes                        |
| UA      | ua_vat     | Ukrainian VAT                                                               | 123456789            | Yes                        |
| UG      | ug_tin     | Uganda Tax Identification Number                                            | 1014751879           | Yes                        |
| UY      | uy_ruc     | Uruguayan RUC number                                                        | 123456789012         | Yes                        |
| UZ      | uz_tin     | Uzbekistan TIN Number                                                       | 123456789            | No                         |
| UZ      | uz_vat     | Uzbekistan VAT Number                                                       | 123456789012         | Yes                        |
| ZA      | za_vat     | South African VAT number                                                    | 4123456789           | Yes                        |
| ZM      | zm_tin     | Zambia Tax Identification Number                                            | 1004751879           | No                         |
| ZW      | zw_tin     | Zimbabwe Tax Identification Number                                          | 1234567890           | No                         |

\*Stripe Tax won't apply tax if this tax ID is provided, in line with the relevant laws.

