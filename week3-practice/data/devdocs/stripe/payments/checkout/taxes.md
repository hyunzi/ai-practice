# Collect taxes

Learn how to collect taxes with Stripe Tax.

# Stripe-hosted page

> This is a Stripe-hosted page for when payment-ui is stripe-hosted. View the full page at https://docs.stripe.com/payments/checkout/taxes?payment-ui=stripe-hosted.

Stripe Tax allows you to calculate the tax on your one-time and recurring payments when you use Checkout. You can enable Stripe Tax to automatically compute taxes on all of your Checkout purchases and subscriptions.

## Create a Checkout Session

You can create Checkout sessions for one-time and recurring purchases.

To calculate tax for new customers, Checkout validates and uses the provided shipping or billing address. For existing customers, Checkout calculates tax by validating and using the attached customer shipping or billing address. If you capture a new billing or shipping address for an existing customer, Checkout won’t automatically override the previous billing or shipping information. You must explicitly request customer address changes.

### Apple Pay and Google Pay

To ensure that Google Pay is offered as a payment method while using Stripe Tax in Checkout, you must either require collecting a shipping address, or provide an existing customer with a saved shipping address. Apple Pay with Stripe Tax displays only when the customer’s browser supports Apple Pay version 12 or greater.

## Calculate tax for new customers

If you don’t pass in an existing customer when creating a Checkout session, Checkout creates a new customer and automatically saves the billing address and shipping information. Checkout uses the shipping address entered during the session to determine the customer’s location for calculating tax. If you don’t collect shipping information, Checkout uses the billing address.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
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
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {enabled: true},
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
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 2}],
  "automatic_tax": {"enabled": True},
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
      'price' => '{{PRICE_ID}}',
      'quantity' => 2,
    ],
  ],
  'automatic_tax' => ['enabled' => true],
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
        .setPrice("{{PRICE_ID}}")
        .setQuantity(2L)
        .build()
    )
    .setAutomaticTax(SessionCreateParams.AutomaticTax.builder().setEnabled(true).build())
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
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {
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
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(2),
    },
  },
  AutomaticTax: &stripe.CheckoutSessionCreateAutomaticTaxParams{
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
            Price = "{{PRICE_ID}}",
            Quantity = 2,
        },
    },
    AutomaticTax = new Stripe.Checkout.SessionAutomaticTaxOptions { Enabled = true },
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

## Optional: Update your products and prices

Stripe Tax uses information stored on *products* (Products represent what your business sells—whether that's a good or a service) and *prices* (Prices define how much and how often to charge for products. This includes how much the product costs, what currency to use, and the interval if the price is for subscriptions) to calculate tax, such as *tax code* (A tax code is the category of your product for tax purposes) and *tax behavior* (Tax behavior determines whether you want to include taxes in the price ("inclusive") or add them on top ("exclusive")). If you don’t explicitly specify these configurations, Stripe Tax will use the default tax code selected in [Tax Settings](https://dashboard.stripe.com/settings/tax).

For more information, see [Specify product tax codes and tax behavior](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md).

## Optional: Calculate tax for existing customers

To calculate tax on an existing customer’s Checkout session, set the `automatic_tax[enabled]` parameter to `true` when you create the session. You can base the tax calculations on the customer’s existing addresses or the new addresses that you collected during checkout:

### Use existing addresses on the customer for taxes

If you’ve already collected your existing customers’ addresses, you can base the tax calculations on those addresses rather than the addresses collected during checkout:

- Which customer address does Checkout use for taxes?

  If available, Checkout uses the customer’s saved [shipping address](https://docs.stripe.com/api/customers/object.md#customer_object-shipping-address) to calculate taxes. Otherwise, Checkout uses the customer’s saved [billing address](https://docs.stripe.com/api/customers/object.md#customer_object-address) to calculate taxes.

- Do the customer addresses have to meet any requirements?

  When using existing addresses for taxes, the customer must either have a valid [shipping address](https://docs.stripe.com/api/customers/object.md#customer_object-shipping-address) or [billing address](https://docs.stripe.com/api/customers/object.md#customer_object-address) saved. You can see whether or not a customer’s saved addresses are valid by checking their [customer.tax.automatic_tax](https://docs.stripe.com/api/customers/object.md#customer_object-tax-automatic_tax) property. If `customer.tax.automatic_tax` value is `supported` or `not_collecting`, the customer’s saved addresses are valid, and you can enable Stripe Tax on Checkout sessions for that customer.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d customer="{{CUSTOMER_ID}}" \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  --customer="{{CUSTOMER_ID}}" \
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
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {enabled: true},
  customer: '{{CUSTOMER_ID}}',
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
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 2}],
  "automatic_tax": {"enabled": True},
  "customer": "{{CUSTOMER_ID}}",
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
      'price' => '{{PRICE_ID}}',
      'quantity' => 2,
    ],
  ],
  'automatic_tax' => ['enabled' => true],
  'customer' => '{{CUSTOMER_ID}}',
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
        .setPrice("{{PRICE_ID}}")
        .setQuantity(2L)
        .build()
    )
    .setAutomaticTax(SessionCreateParams.AutomaticTax.builder().setEnabled(true).build())
    .setCustomer("{{CUSTOMER_ID}}")
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
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {
    enabled: true,
  },
  customer: '{{CUSTOMER_ID}}',
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
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(2),
    },
  },
  AutomaticTax: &stripe.CheckoutSessionCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
  },
  Customer: stripe.String("{{CUSTOMER_ID}}"),
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
            Price = "{{PRICE_ID}}",
            Quantity = 2,
        },
    },
    AutomaticTax = new Stripe.Checkout.SessionAutomaticTaxOptions { Enabled = true },
    Customer = "{{CUSTOMER_ID}}",
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

### Use addresses collected during Checkout for taxes

You can configure Checkout to save a customer’s new billing or shipping addresses. In this case, Checkout calculates the tax by using the address entered during checkout.

- Which address does Checkout use for taxes?

  If you’re [collecting shipping addresses](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-shipping_address_collection), Checkout uses the shipping address entered during the session to calculate taxes. Otherwise, Checkout uses the billing address entered during the session to calculate taxes.

- Where are the addresses collected during Checkout saved?

  If you’re [collecting shipping addresses](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-shipping_address_collection), Checkout saves the shipping address entered during the session to the customer’s [customer.shipping.address](https://docs.stripe.com/api/customers/object.md#customer_object-shipping-address) property. Otherwise, Checkout saves the billing address entered during the session to the customer’s [customer.address](https://docs.stripe.com/api/customers/object.md#customer_object-address) property. In both cases, the address used for taxes will override any existing addresses.

If you’re collecting shipping addresses with Checkout, set the `customer_update[shipping]` property to `auto`. This allows you to copy the shipping information from Checkout to the customer.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d customer="{{CUSTOMER_ID}}" \
  -d "customer_update[shipping]"=auto \
  -d "shipping_address_collection[allowed_countries][0]"=US \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  --customer="{{CUSTOMER_ID}}" \
  -d "customer_update[shipping]"=auto \
  -d "shipping_address_collection[allowed_countries][0]"=US \
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
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {enabled: true},
  customer: '{{CUSTOMER_ID}}',
  customer_update: {shipping: 'auto'},
  shipping_address_collection: {allowed_countries: ['US']},
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
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 2}],
  "automatic_tax": {"enabled": True},
  "customer": "{{CUSTOMER_ID}}",
  "customer_update": {"shipping": "auto"},
  "shipping_address_collection": {"allowed_countries": ["US"]},
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
      'price' => '{{PRICE_ID}}',
      'quantity' => 2,
    ],
  ],
  'automatic_tax' => ['enabled' => true],
  'customer' => '{{CUSTOMER_ID}}',
  'customer_update' => ['shipping' => 'auto'],
  'shipping_address_collection' => ['allowed_countries' => ['US']],
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
        .setPrice("{{PRICE_ID}}")
        .setQuantity(2L)
        .build()
    )
    .setAutomaticTax(SessionCreateParams.AutomaticTax.builder().setEnabled(true).build())
    .setCustomer("{{CUSTOMER_ID}}")
    .setCustomerUpdate(
      SessionCreateParams.CustomerUpdate.builder()
        .setShipping(SessionCreateParams.CustomerUpdate.Shipping.AUTO)
        .build()
    )
    .setShippingAddressCollection(
      SessionCreateParams.ShippingAddressCollection.builder()
        .addAllowedCountry(
          SessionCreateParams.ShippingAddressCollection.AllowedCountry.US
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
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {
    enabled: true,
  },
  customer: '{{CUSTOMER_ID}}',
  customer_update: {
    shipping: 'auto',
  },
  shipping_address_collection: {
    allowed_countries: ['US'],
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
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(2),
    },
  },
  AutomaticTax: &stripe.CheckoutSessionCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
  },
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  CustomerUpdate: &stripe.CheckoutSessionCreateCustomerUpdateParams{
    Shipping: stripe.String("auto"),
  },
  ShippingAddressCollection: &stripe.CheckoutSessionCreateShippingAddressCollectionParams{
    AllowedCountries: []*string{stripe.String("US")},
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
            Price = "{{PRICE_ID}}",
            Quantity = 2,
        },
    },
    AutomaticTax = new Stripe.Checkout.SessionAutomaticTaxOptions { Enabled = true },
    Customer = "{{CUSTOMER_ID}}",
    CustomerUpdate = new Stripe.Checkout.SessionCustomerUpdateOptions
    {
        Shipping = "auto",
    },
    ShippingAddressCollection = new Stripe.Checkout.SessionShippingAddressCollectionOptions
    {
        AllowedCountries = new List<string> { "US" },
    },
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

If you aren’t collecting shipping addresses with Checkout, and you want to use billing addresses entered during checkout for taxes, you must save the billing address to the customer. Set the `customer_update[address]` property to `auto` so that you copy the newly-entered address onto the provided customer.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d customer="{{CUSTOMER_ID}}" \
  -d "customer_update[shipping]"=auto \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  --customer="{{CUSTOMER_ID}}" \
  -d "customer_update[shipping]"=auto \
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
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {enabled: true},
  customer: '{{CUSTOMER_ID}}',
  customer_update: {shipping: 'auto'},
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
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 2}],
  "automatic_tax": {"enabled": True},
  "customer": "{{CUSTOMER_ID}}",
  "customer_update": {"shipping": "auto"},
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
      'price' => '{{PRICE_ID}}',
      'quantity' => 2,
    ],
  ],
  'automatic_tax' => ['enabled' => true],
  'customer' => '{{CUSTOMER_ID}}',
  'customer_update' => ['shipping' => 'auto'],
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
        .setPrice("{{PRICE_ID}}")
        .setQuantity(2L)
        .build()
    )
    .setAutomaticTax(SessionCreateParams.AutomaticTax.builder().setEnabled(true).build())
    .setCustomer("{{CUSTOMER_ID}}")
    .setCustomerUpdate(
      SessionCreateParams.CustomerUpdate.builder()
        .setShipping(SessionCreateParams.CustomerUpdate.Shipping.AUTO)
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
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {
    enabled: true,
  },
  customer: '{{CUSTOMER_ID}}',
  customer_update: {
    shipping: 'auto',
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
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(2),
    },
  },
  AutomaticTax: &stripe.CheckoutSessionCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
  },
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  CustomerUpdate: &stripe.CheckoutSessionCreateCustomerUpdateParams{
    Shipping: stripe.String("auto"),
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
            Price = "{{PRICE_ID}}",
            Quantity = 2,
        },
    },
    AutomaticTax = new Stripe.Checkout.SessionAutomaticTaxOptions { Enabled = true },
    Customer = "{{CUSTOMER_ID}}",
    CustomerUpdate = new Stripe.Checkout.SessionCustomerUpdateOptions
    {
        Shipping = "auto",
    },
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

## Optional: Check the response

To see the results of the latest tax calculation, the [total_details.amount_tax](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-total_details) property in the Checkout Session resource shows the calculated tax amount. Additionally, you can use the [Dashboard](https://dashboard.stripe.com/) to view the tax outcome for each payment.


# Embedded form

> This is a Embedded form for when payment-ui is embedded-form. View the full page at https://docs.stripe.com/payments/checkout/taxes?payment-ui=embedded-form.

Stripe Tax allows you to calculate the tax on your one-time and recurring payments when you use Checkout. You can enable Stripe Tax to automatically compute taxes on all of your Checkout purchases and subscriptions.

## Create a Checkout Session

After updating your products and prices, you’re ready to start calculating tax on your Checkout sessions. You can create sessions for one-time and recurring purchases.

To calculate tax for new customers, Checkout validates and uses the provided shipping or billing address. For existing customers, Checkout calculates tax by validating and using the attached customer shipping or billing address. If you capture a new billing or shipping address for an existing customer, Checkout won’t automatically override the previous billing or shipping information. You must explicitly request customer address changes.

### Apple Pay and Google Pay

To ensure that Google Pay is offered as a payment method while using Stripe Tax in Checkout, you must either require collecting a shipping address, or provide an existing customer with a saved shipping address. Apple Pay with Stripe Tax displays only when the customer’s browser supports Apple Pay version 12 or greater.

## Calculate tax for new customers

If you don’t pass in an existing customer when creating a Checkout session, Checkout creates a new customer and automatically saves the billing address and shipping information. Checkout uses the shipping address entered during the session to determine the customer’s location for calculating tax. If you don’t collect shipping information, Checkout uses the billing address.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
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
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {enabled: true},
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
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 2}],
  "automatic_tax": {"enabled": True},
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
      'price' => '{{PRICE_ID}}',
      'quantity' => 2,
    ],
  ],
  'automatic_tax' => ['enabled' => true],
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
        .setPrice("{{PRICE_ID}}")
        .setQuantity(2L)
        .build()
    )
    .setAutomaticTax(SessionCreateParams.AutomaticTax.builder().setEnabled(true).build())
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
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {
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
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(2),
    },
  },
  AutomaticTax: &stripe.CheckoutSessionCreateAutomaticTaxParams{
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
            Price = "{{PRICE_ID}}",
            Quantity = 2,
        },
    },
    AutomaticTax = new Stripe.Checkout.SessionAutomaticTaxOptions { Enabled = true },
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

## Optional: Update your products and prices

Stripe Tax uses information stored on *products* (Products represent what your business sells—whether that's a good or a service) and *prices* (Prices define how much and how often to charge for products. This includes how much the product costs, what currency to use, and the interval if the price is for subscriptions) to calculate tax, such as *tax code* (A tax code is the category of your product for tax purposes) and *tax behavior* (Tax behavior determines whether you want to include taxes in the price ("inclusive") or add them on top ("exclusive")). If you don’t explicitly specify these configurations, Stripe Tax will use the default tax code selected in [Tax Settings](https://dashboard.stripe.com/settings/tax).

For more information, see [Specify product tax codes and tax behavior](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md).

## Optional: Calculate tax for existing customers

To calculate tax on an existing customer’s Checkout session, set the `automatic_tax[enabled]` parameter to `true` when you create the session. You can base the tax calculations on the customer’s existing addresses or the new addresses that you collected during checkout:

### Use existing addresses on the customer for taxes

If you’ve already collected your existing customers’ addresses, you can base the tax calculations on those addresses rather than the addresses collected during checkout:

- Which customer address does Checkout use for taxes?

  If available, Checkout uses the customer’s saved [shipping address](https://docs.stripe.com/api/customers/object.md#customer_object-shipping-address) to calculate taxes. Otherwise, Checkout uses the customer’s saved [billing address](https://docs.stripe.com/api/customers/object.md#customer_object-address) to calculate taxes.

- Do the customer addresses have to meet any requirements?

  When using existing addresses for taxes, the customer must either have a valid [shipping address](https://docs.stripe.com/api/customers/object.md#customer_object-shipping-address) or [billing address](https://docs.stripe.com/api/customers/object.md#customer_object-address) saved. You can see whether or not a customer’s saved addresses are valid by checking their [customer.tax.automatic_tax](https://docs.stripe.com/api/customers/object.md#customer_object-tax-automatic_tax) property. If `customer.tax.automatic_tax` value is `supported` or `not_collecting`, the customer’s saved addresses are valid, and you can enable Stripe Tax on Checkout sessions for that customer.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d customer="{{CUSTOMER_ID}}" \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  --customer="{{CUSTOMER_ID}}" \
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
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {enabled: true},
  customer: '{{CUSTOMER_ID}}',
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
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 2}],
  "automatic_tax": {"enabled": True},
  "customer": "{{CUSTOMER_ID}}",
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
      'price' => '{{PRICE_ID}}',
      'quantity' => 2,
    ],
  ],
  'automatic_tax' => ['enabled' => true],
  'customer' => '{{CUSTOMER_ID}}',
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
        .setPrice("{{PRICE_ID}}")
        .setQuantity(2L)
        .build()
    )
    .setAutomaticTax(SessionCreateParams.AutomaticTax.builder().setEnabled(true).build())
    .setCustomer("{{CUSTOMER_ID}}")
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
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {
    enabled: true,
  },
  customer: '{{CUSTOMER_ID}}',
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
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(2),
    },
  },
  AutomaticTax: &stripe.CheckoutSessionCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
  },
  Customer: stripe.String("{{CUSTOMER_ID}}"),
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
            Price = "{{PRICE_ID}}",
            Quantity = 2,
        },
    },
    AutomaticTax = new Stripe.Checkout.SessionAutomaticTaxOptions { Enabled = true },
    Customer = "{{CUSTOMER_ID}}",
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

### Use addresses collected during Checkout for taxes

You can configure Checkout to save a customer’s new billing or shipping addresses. In this case, Checkout calculates the tax by using the address entered during checkout.

- Which address does Checkout use for taxes?

  If you’re [collecting shipping addresses](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-shipping_address_collection), Checkout uses the shipping address entered during the session to calculate taxes. Otherwise, Checkout uses the billing address entered during the session to calculate taxes.

- Where are the addresses collected during Checkout saved?

  If you’re [collecting shipping addresses](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-shipping_address_collection), Checkout saves the shipping address entered during the session to the customer’s [customer.shipping.address](https://docs.stripe.com/api/customers/object.md#customer_object-shipping-address) property. Otherwise, Checkout saves the billing address entered during the session to the customer’s [customer.address](https://docs.stripe.com/api/customers/object.md#customer_object-address) property. In both cases, the address used for taxes will override any existing addresses.

If you’re collecting shipping addresses with Checkout, set the `customer_update[shipping]` property to `auto`. This allows you to copy the shipping information from Checkout to the customer.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d customer="{{CUSTOMER_ID}}" \
  -d "customer_update[shipping]"=auto \
  -d "shipping_address_collection[allowed_countries][0]"=US \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  --customer="{{CUSTOMER_ID}}" \
  -d "customer_update[shipping]"=auto \
  -d "shipping_address_collection[allowed_countries][0]"=US \
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
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {enabled: true},
  customer: '{{CUSTOMER_ID}}',
  customer_update: {shipping: 'auto'},
  shipping_address_collection: {allowed_countries: ['US']},
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
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 2}],
  "automatic_tax": {"enabled": True},
  "customer": "{{CUSTOMER_ID}}",
  "customer_update": {"shipping": "auto"},
  "shipping_address_collection": {"allowed_countries": ["US"]},
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
      'price' => '{{PRICE_ID}}',
      'quantity' => 2,
    ],
  ],
  'automatic_tax' => ['enabled' => true],
  'customer' => '{{CUSTOMER_ID}}',
  'customer_update' => ['shipping' => 'auto'],
  'shipping_address_collection' => ['allowed_countries' => ['US']],
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
        .setPrice("{{PRICE_ID}}")
        .setQuantity(2L)
        .build()
    )
    .setAutomaticTax(SessionCreateParams.AutomaticTax.builder().setEnabled(true).build())
    .setCustomer("{{CUSTOMER_ID}}")
    .setCustomerUpdate(
      SessionCreateParams.CustomerUpdate.builder()
        .setShipping(SessionCreateParams.CustomerUpdate.Shipping.AUTO)
        .build()
    )
    .setShippingAddressCollection(
      SessionCreateParams.ShippingAddressCollection.builder()
        .addAllowedCountry(
          SessionCreateParams.ShippingAddressCollection.AllowedCountry.US
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
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {
    enabled: true,
  },
  customer: '{{CUSTOMER_ID}}',
  customer_update: {
    shipping: 'auto',
  },
  shipping_address_collection: {
    allowed_countries: ['US'],
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
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(2),
    },
  },
  AutomaticTax: &stripe.CheckoutSessionCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
  },
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  CustomerUpdate: &stripe.CheckoutSessionCreateCustomerUpdateParams{
    Shipping: stripe.String("auto"),
  },
  ShippingAddressCollection: &stripe.CheckoutSessionCreateShippingAddressCollectionParams{
    AllowedCountries: []*string{stripe.String("US")},
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
            Price = "{{PRICE_ID}}",
            Quantity = 2,
        },
    },
    AutomaticTax = new Stripe.Checkout.SessionAutomaticTaxOptions { Enabled = true },
    Customer = "{{CUSTOMER_ID}}",
    CustomerUpdate = new Stripe.Checkout.SessionCustomerUpdateOptions
    {
        Shipping = "auto",
    },
    ShippingAddressCollection = new Stripe.Checkout.SessionShippingAddressCollectionOptions
    {
        AllowedCountries = new List<string> { "US" },
    },
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

If you aren’t collecting shipping addresses with Checkout, and you want to use billing addresses entered during checkout for taxes, you must save the billing address to the customer. Set the `customer_update[address]` property to `auto` so that you copy the newly-entered address onto the provided customer.

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  -d customer="{{CUSTOMER_ID}}" \
  -d "customer_update[shipping]"=auto \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=2 \
  -d "automatic_tax[enabled]"=true \
  --customer="{{CUSTOMER_ID}}" \
  -d "customer_update[shipping]"=auto \
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
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {enabled: true},
  customer: '{{CUSTOMER_ID}}',
  customer_update: {shipping: 'auto'},
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
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 2}],
  "automatic_tax": {"enabled": True},
  "customer": "{{CUSTOMER_ID}}",
  "customer_update": {"shipping": "auto"},
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
      'price' => '{{PRICE_ID}}',
      'quantity' => 2,
    ],
  ],
  'automatic_tax' => ['enabled' => true],
  'customer' => '{{CUSTOMER_ID}}',
  'customer_update' => ['shipping' => 'auto'],
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
        .setPrice("{{PRICE_ID}}")
        .setQuantity(2L)
        .build()
    )
    .setAutomaticTax(SessionCreateParams.AutomaticTax.builder().setEnabled(true).build())
    .setCustomer("{{CUSTOMER_ID}}")
    .setCustomerUpdate(
      SessionCreateParams.CustomerUpdate.builder()
        .setShipping(SessionCreateParams.CustomerUpdate.Shipping.AUTO)
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
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 2,
    },
  ],
  automatic_tax: {
    enabled: true,
  },
  customer: '{{CUSTOMER_ID}}',
  customer_update: {
    shipping: 'auto',
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
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(2),
    },
  },
  AutomaticTax: &stripe.CheckoutSessionCreateAutomaticTaxParams{
    Enabled: stripe.Bool(true),
  },
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  CustomerUpdate: &stripe.CheckoutSessionCreateCustomerUpdateParams{
    Shipping: stripe.String("auto"),
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
            Price = "{{PRICE_ID}}",
            Quantity = 2,
        },
    },
    AutomaticTax = new Stripe.Checkout.SessionAutomaticTaxOptions { Enabled = true },
    Customer = "{{CUSTOMER_ID}}",
    CustomerUpdate = new Stripe.Checkout.SessionCustomerUpdateOptions
    {
        Shipping = "auto",
    },
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

## Optional: Check the response

To see the results of the latest tax calculation, the [total_details.amount_tax](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-total_details) property in the Checkout Session resource shows the calculated tax amount. Additionally, you can use the [Dashboard](https://dashboard.stripe.com/) to view the tax outcome for each payment.

