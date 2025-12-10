# Charge for shipping

Create different shipping rates for your customers.

Shipping rates let you display various shipping options—like standard, express, and overnight—with more accurate delivery estimates. Charge your customer for shipping using different Stripe products, some of which require coding. Before you create a shipping rate, learn how to [collect billing and shipping addresses](https://docs.stripe.com/payments/no-code/collect-addresses.md).

> #### Third-party plugins
> 
> If you’re using a third-party application with Stripe (for example, [Thrivecart](https://support.thrivecart.com/help/setting-your-physical-fulfilment-shipping-options/) or [Shopify](https://help.shopify.com/en/manual/shipping/setting-up-and-managing-your-shipping/setting-up-shipping-rates)) and want to adjust the shipping rate, visit the docs for that service.

#### Dashboard

1. Create a [payment link](https://dashboard.stripe.com/test/payment-links/create) and select **Collect customers’ addresses** with the **Billing and shipping addresses** option.
1. Select the countries you ship to.
1. Click **Add shipping rates** to select an existing shipping rate or add a new one. You can only use shipping rates with one-time prices on payment links.
![](https://b.stripecdn.com/docs-statics-srv/assets/create-payment-link-with-shipping-rate.299819920f996e92c28c393f7a9d91cc.png)

Add a new shipping rate for a payment link in the Dashboard

#### API

[Create a shipping rate](https://docs.stripe.com/api/shipping_rates.md), which at a minimum, requires the `type` and `display_name` parameters. The following code sample uses both of these parameters along with `fixed_amount` and `deliver_estimate` to create a shipping rate:

```curl
curl https://api.stripe.com/v1/shipping_rates \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d display_name="Ground shipping" \
  -d type=fixed_amount \
  -d "fixed_amount[amount]"=500 \
  -d "fixed_amount[currency]"=usd \
  -d "delivery_estimate[minimum][unit]"=business_day \
  -d "delivery_estimate[minimum][value]"=5 \
  -d "delivery_estimate[maximum][unit]"=business_day \
  -d "delivery_estimate[maximum][value]"=7
```

```cli
stripe shipping_rates create  \
  --display-name="Ground shipping" \
  --type=fixed_amount \
  -d "fixed_amount[amount]"=500 \
  -d "fixed_amount[currency]"=usd \
  -d "delivery_estimate[minimum][unit]"=business_day \
  -d "delivery_estimate[minimum][value]"=5 \
  -d "delivery_estimate[maximum][unit]"=business_day \
  -d "delivery_estimate[maximum][value]"=7
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

shipping_rate = client.v1.shipping_rates.create({
  display_name: 'Ground shipping',
  type: 'fixed_amount',
  fixed_amount: {
    amount: 500,
    currency: 'usd',
  },
  delivery_estimate: {
    minimum: {
      unit: 'business_day',
      value: 5,
    },
    maximum: {
      unit: 'business_day',
      value: 7,
    },
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
shipping_rate = client.v1.shipping_rates.create({
  "display_name": "Ground shipping",
  "type": "fixed_amount",
  "fixed_amount": {"amount": 500, "currency": "usd"},
  "delivery_estimate": {
    "minimum": {"unit": "business_day", "value": 5},
    "maximum": {"unit": "business_day", "value": 7},
  },
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$shippingRate = $stripe->shippingRates->create([
  'display_name' => 'Ground shipping',
  'type' => 'fixed_amount',
  'fixed_amount' => [
    'amount' => 500,
    'currency' => 'usd',
  ],
  'delivery_estimate' => [
    'minimum' => [
      'unit' => 'business_day',
      'value' => 5,
    ],
    'maximum' => [
      'unit' => 'business_day',
      'value' => 7,
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

ShippingRateCreateParams params =
  ShippingRateCreateParams.builder()
    .setDisplayName("Ground shipping")
    .setType(ShippingRateCreateParams.Type.FIXED_AMOUNT)
    .setFixedAmount(
      ShippingRateCreateParams.FixedAmount.builder()
        .setAmount(500L)
        .setCurrency("usd")
        .build()
    )
    .setDeliveryEstimate(
      ShippingRateCreateParams.DeliveryEstimate.builder()
        .setMinimum(
          ShippingRateCreateParams.DeliveryEstimate.Minimum.builder()
            .setUnit(ShippingRateCreateParams.DeliveryEstimate.Minimum.Unit.BUSINESS_DAY)
            .setValue(5L)
            .build()
        )
        .setMaximum(
          ShippingRateCreateParams.DeliveryEstimate.Maximum.builder()
            .setUnit(ShippingRateCreateParams.DeliveryEstimate.Maximum.Unit.BUSINESS_DAY)
            .setValue(7L)
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
ShippingRate shippingRate = client.v1().shippingRates().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const shippingRate = await stripe.shippingRates.create({
  display_name: 'Ground shipping',
  type: 'fixed_amount',
  fixed_amount: {
    amount: 500,
    currency: 'usd',
  },
  delivery_estimate: {
    minimum: {
      unit: 'business_day',
      value: 5,
    },
    maximum: {
      unit: 'business_day',
      value: 7,
    },
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.ShippingRateCreateParams{
  DisplayName: stripe.String("Ground shipping"),
  Type: stripe.String("fixed_amount"),
  FixedAmount: &stripe.ShippingRateCreateFixedAmountParams{
    Amount: stripe.Int64(500),
    Currency: stripe.String(stripe.CurrencyUSD),
  },
  DeliveryEstimate: &stripe.ShippingRateCreateDeliveryEstimateParams{
    Minimum: &stripe.ShippingRateCreateDeliveryEstimateMinimumParams{
      Unit: stripe.String(stripe.ShippingRateDeliveryEstimateMinimumUnitBusinessDay),
      Value: stripe.Int64(5),
    },
    Maximum: &stripe.ShippingRateCreateDeliveryEstimateMaximumParams{
      Unit: stripe.String(stripe.ShippingRateDeliveryEstimateMaximumUnitBusinessDay),
      Value: stripe.Int64(7),
    },
  },
}
result, err := sc.V1ShippingRates.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new ShippingRateCreateOptions
{
    DisplayName = "Ground shipping",
    Type = "fixed_amount",
    FixedAmount = new ShippingRateFixedAmountOptions { Amount = 500, Currency = "usd" },
    DeliveryEstimate = new ShippingRateDeliveryEstimateOptions
    {
        Minimum = new ShippingRateDeliveryEstimateMinimumOptions
        {
            Unit = "business_day",
            Value = 5,
        },
        Maximum = new ShippingRateDeliveryEstimateMaximumOptions
        {
            Unit = "business_day",
            Value = 7,
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.ShippingRates;
ShippingRate shippingRate = service.Create(options);
```

Create a payment link and [collect a billing and shipping address](https://docs.stripe.com/payments/collect-addresses.md?payment-ui=payment-links). Add shipping rates to the payment link using the [shipping_options](https://docs.stripe.com/api/payment_links/payment_links/object.md#payment_link_object-shipping_options) parameter. You can only use shipping rates with one time prices on payment links.

```curl
curl https://api.stripe.com/v1/payment_links \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  -d billing_address_collection=required \
  -d "shipping_address_collection[allowed_countries][0]"=US \
  -d "shipping_options[0][shipping_rate]"="{{SHIPPINGRATE_ID}}"
```

```cli
stripe payment_links create  \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1 \
  --billing-address-collection=required \
  -d "shipping_address_collection[allowed_countries][0]"=US \
  -d "shipping_options[0][shipping_rate]"="{{SHIPPINGRATE_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_link = client.v1.payment_links.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  billing_address_collection: 'required',
  shipping_address_collection: {allowed_countries: ['US']},
  shipping_options: [{shipping_rate: '{{SHIPPINGRATE_ID}}'}],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_link = client.v1.payment_links.create({
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
  "billing_address_collection": "required",
  "shipping_address_collection": {"allowed_countries": ["US"]},
  "shipping_options": [{"shipping_rate": "{{SHIPPINGRATE_ID}}"}],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentLink = $stripe->paymentLinks->create([
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
  'billing_address_collection' => 'required',
  'shipping_address_collection' => ['allowed_countries' => ['US']],
  'shipping_options' => [['shipping_rate' => '{{SHIPPINGRATE_ID}}']],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PaymentLinkCreateParams params =
  PaymentLinkCreateParams.builder()
    .addLineItem(
      PaymentLinkCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .setBillingAddressCollection(
      PaymentLinkCreateParams.BillingAddressCollection.REQUIRED
    )
    .setShippingAddressCollection(
      PaymentLinkCreateParams.ShippingAddressCollection.builder()
        .addAllowedCountry(
          PaymentLinkCreateParams.ShippingAddressCollection.AllowedCountry.US
        )
        .build()
    )
    .addShippingOption(
      PaymentLinkCreateParams.ShippingOption.builder()
        .setShippingRate("{{SHIPPINGRATE_ID}}")
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
PaymentLink paymentLink = client.v1().paymentLinks().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const paymentLink = await stripe.paymentLinks.create({
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
  billing_address_collection: 'required',
  shipping_address_collection: {
    allowed_countries: ['US'],
  },
  shipping_options: [
    {
      shipping_rate: '{{SHIPPINGRATE_ID}}',
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PaymentLinkCreateParams{
  LineItems: []*stripe.PaymentLinkCreateLineItemParams{
    &stripe.PaymentLinkCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
  },
  BillingAddressCollection: stripe.String(stripe.PaymentLinkBillingAddressCollectionRequired),
  ShippingAddressCollection: &stripe.PaymentLinkCreateShippingAddressCollectionParams{
    AllowedCountries: []*string{stripe.String("US")},
  },
  ShippingOptions: []*stripe.PaymentLinkCreateShippingOptionParams{
    &stripe.PaymentLinkCreateShippingOptionParams{
      ShippingRate: stripe.String("{{SHIPPINGRATE_ID}}"),
    },
  },
}
result, err := sc.V1PaymentLinks.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PaymentLinkCreateOptions
{
    LineItems = new List<PaymentLinkLineItemOptions>
    {
        new PaymentLinkLineItemOptions { Price = "{{PRICE_ID}}", Quantity = 1 },
    },
    BillingAddressCollection = "required",
    ShippingAddressCollection = new PaymentLinkShippingAddressCollectionOptions
    {
        AllowedCountries = new List<string> { "US" },
    },
    ShippingOptions = new List<PaymentLinkShippingOptionOptions>
    {
        new PaymentLinkShippingOptionOptions
        {
            ShippingRate = "{{SHIPPINGRATE_ID}}",
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentLinks;
PaymentLink paymentLink = service.Create(options);
```
