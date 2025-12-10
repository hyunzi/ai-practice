# Charge for shipping

Create different shipping rates for your customers.

Shipping rates let you display various shipping options—like standard, express, and overnight—with more accurate delivery estimates. Charge your customer for shipping using different Stripe products. Before you create a shipping rate, learn how to [collect billing and shipping addresses](https://docs.stripe.com/payments/collect-addresses.md).

## Create a shipping rate [Dashboard] [Server-side]

Shipping rates only support fixed amount values for the entire order. You can’t adjust the shipping rate based on the number of items in the order.

#### Dashboard

To add a [shipping rate](https://dashboard.stripe.com/test/shipping-rates) using the Dashboard:

1. Click **Create shipping rate**.
1. Enter an amount, a description, and an optional delivery estimate.
1. Click **Save**, and copy the shipping rate ID (`shr_123456`).
![](https://b.stripecdn.com/docs-statics-srv/assets/create-shipping-rate-dashboard.ddd79821d5edee523d7da9d22682be59.png)

Enter your shipping rate details

### Update a shipping rate

You can’t update a shipping rate in the Dashboard. To update a shipping rate in the Dashboard, you must archive the shipping rate and then create a new one.

### Archive a shipping rate

To archive a shipping rate:

1. On the [Shipping rates](https://dashboard.stripe.com/test/shipping-rates) tab, select the applicable shipping rate.
1. Click the overflow menu ⋯, and select **Archive**.

To unarchive the shipping rate, click the overflow menu ⋯, and select **Unarchive shipping rate**.

#### API

> #### Interested in dynamic shipping rate updates?
> 
> Checkout supports letting you dynamically update shipping rates based on the address your customer provides or the value of the order. See [Dynamically customize shipping options](https://docs.stripe.com/payments/checkout/custom-shipping-options.md) about this preview feature.

[Create a shipping rate](https://docs.stripe.com/api/shipping_rates.md), which at a minimum, requires the `type` and `display_name` parameters. The following code sample uses both of these parameters along with `fixed_amount` and `delivery_estimate` to create a shipping rate:

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

### Update a shipping rate

To [update a shipping rate](https://docs.stripe.com/api/shipping_rates/update.md), call `Stripe::ShippingRate.update`, and update the parameters as needed.

## Create a Checkout Session [Server-side]

To create a Checkout Session that includes your shipping rate, pass in the generated shipping rate ID to the [shipping_options](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-shipping_options) parameter. If you want to create the shipping rate at the same time as a Checkout Session, use the `shipping_rate_data` parameter with `shipping_options`. Only Checkout Sessions in [payment mode](https://docs.stripe.com/api/checkout/sessions/create.md#create_checkout_session-mode) support shipping options.

The following code sample adds two shipping options to the Checkout Session:

- Free shipping, with an estimated delivery of 5-7 business days.
- Next day air, at a cost of 15.00 USD, with an estimated delivery of exactly 1 business day.

In this example, the first option in the `shipping_options` array is pre-selected for the customer on the checkout page. However, customers can choose either option.

#### Stripe-hosted page

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "shipping_address_collection[allowed_countries][0]"=US \
  -d "shipping_address_collection[allowed_countries][1]"=CA \
  -d "shipping_options[0][shipping_rate_data][type]"=fixed_amount \
  -d "shipping_options[0][shipping_rate_data][fixed_amount][amount]"=0 \
  -d "shipping_options[0][shipping_rate_data][fixed_amount][currency]"=usd \
  -d "shipping_options[0][shipping_rate_data][display_name]"="Free shipping" \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][minimum][unit]"=business_day \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][minimum][value]"=5 \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][maximum][unit]"=business_day \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][maximum][value]"=7 \
  -d "shipping_options[1][shipping_rate_data][type]"=fixed_amount \
  -d "shipping_options[1][shipping_rate_data][fixed_amount][amount]"=1500 \
  -d "shipping_options[1][shipping_rate_data][fixed_amount][currency]"=usd \
  -d "shipping_options[1][shipping_rate_data][display_name]"="Next day air" \
  -d "shipping_options[1][shipping_rate_data][delivery_estimate][minimum][unit]"=business_day \
  -d "shipping_options[1][shipping_rate_data][delivery_estimate][minimum][value]"=1 \
  -d "shipping_options[1][shipping_rate_data][delivery_estimate][maximum][unit]"=business_day \
  -d "shipping_options[1][shipping_rate_data][delivery_estimate][maximum][value]"=1 \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][unit_amount]"=2000 \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  -d "shipping_address_collection[allowed_countries][0]"=US \
  -d "shipping_address_collection[allowed_countries][1]"=CA \
  -d "shipping_options[0][shipping_rate_data][type]"=fixed_amount \
  -d "shipping_options[0][shipping_rate_data][fixed_amount][amount]"=0 \
  -d "shipping_options[0][shipping_rate_data][fixed_amount][currency]"=usd \
  -d "shipping_options[0][shipping_rate_data][display_name]"="Free shipping" \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][minimum][unit]"=business_day \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][minimum][value]"=5 \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][maximum][unit]"=business_day \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][maximum][value]"=7 \
  -d "shipping_options[1][shipping_rate_data][type]"=fixed_amount \
  -d "shipping_options[1][shipping_rate_data][fixed_amount][amount]"=1500 \
  -d "shipping_options[1][shipping_rate_data][fixed_amount][currency]"=usd \
  -d "shipping_options[1][shipping_rate_data][display_name]"="Next day air" \
  -d "shipping_options[1][shipping_rate_data][delivery_estimate][minimum][unit]"=business_day \
  -d "shipping_options[1][shipping_rate_data][delivery_estimate][minimum][value]"=1 \
  -d "shipping_options[1][shipping_rate_data][delivery_estimate][maximum][unit]"=business_day \
  -d "shipping_options[1][shipping_rate_data][delivery_estimate][maximum][value]"=1 \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][unit_amount]"=2000 \
  -d "line_items[0][quantity]"=1 \
  --mode=payment \
  --success-url="https://example.com/success"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  shipping_address_collection: {allowed_countries: ['US', 'CA']},
  shipping_options: [
    {
      shipping_rate_data: {
        type: 'fixed_amount',
        fixed_amount: {
          amount: 0,
          currency: 'usd',
        },
        display_name: 'Free shipping',
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
      },
    },
    {
      shipping_rate_data: {
        type: 'fixed_amount',
        fixed_amount: {
          amount: 1500,
          currency: 'usd',
        },
        display_name: 'Next day air',
        delivery_estimate: {
          minimum: {
            unit: 'business_day',
            value: 1,
          },
          maximum: {
            unit: 'business_day',
            value: 1,
          },
        },
      },
    },
  ],
  line_items: [
    {
      price_data: {
        currency: 'usd',
        product_data: {name: 'T-shirt'},
        unit_amount: 2000,
      },
      quantity: 1,
    },
  ],
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
  "shipping_address_collection": {"allowed_countries": ["US", "CA"]},
  "shipping_options": [
    {
      "shipping_rate_data": {
        "type": "fixed_amount",
        "fixed_amount": {"amount": 0, "currency": "usd"},
        "display_name": "Free shipping",
        "delivery_estimate": {
          "minimum": {"unit": "business_day", "value": 5},
          "maximum": {"unit": "business_day", "value": 7},
        },
      },
    },
    {
      "shipping_rate_data": {
        "type": "fixed_amount",
        "fixed_amount": {"amount": 1500, "currency": "usd"},
        "display_name": "Next day air",
        "delivery_estimate": {
          "minimum": {"unit": "business_day", "value": 1},
          "maximum": {"unit": "business_day", "value": 1},
        },
      },
    },
  ],
  "line_items": [
    {
      "price_data": {
        "currency": "usd",
        "product_data": {"name": "T-shirt"},
        "unit_amount": 2000,
      },
      "quantity": 1,
    },
  ],
  "mode": "payment",
  "success_url": "https://example.com/success",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$session = $stripe->checkout->sessions->create([
  'shipping_address_collection' => ['allowed_countries' => ['US', 'CA']],
  'shipping_options' => [
    [
      'shipping_rate_data' => [
        'type' => 'fixed_amount',
        'fixed_amount' => [
          'amount' => 0,
          'currency' => 'usd',
        ],
        'display_name' => 'Free shipping',
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
      ],
    ],
    [
      'shipping_rate_data' => [
        'type' => 'fixed_amount',
        'fixed_amount' => [
          'amount' => 1500,
          'currency' => 'usd',
        ],
        'display_name' => 'Next day air',
        'delivery_estimate' => [
          'minimum' => [
            'unit' => 'business_day',
            'value' => 1,
          ],
          'maximum' => [
            'unit' => 'business_day',
            'value' => 1,
          ],
        ],
      ],
    ],
  ],
  'line_items' => [
    [
      'price_data' => [
        'currency' => 'usd',
        'product_data' => ['name' => 'T-shirt'],
        'unit_amount' => 2000,
      ],
      'quantity' => 1,
    ],
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
    .setShippingAddressCollection(
      SessionCreateParams.ShippingAddressCollection.builder()
        .addAllowedCountry(
          SessionCreateParams.ShippingAddressCollection.AllowedCountry.US
        )
        .addAllowedCountry(
          SessionCreateParams.ShippingAddressCollection.AllowedCountry.CA
        )
        .build()
    )
    .addShippingOption(
      SessionCreateParams.ShippingOption.builder()
        .setShippingRateData(
          SessionCreateParams.ShippingOption.ShippingRateData.builder()
            .setType(
              SessionCreateParams.ShippingOption.ShippingRateData.Type.FIXED_AMOUNT
            )
            .setFixedAmount(
              SessionCreateParams.ShippingOption.ShippingRateData.FixedAmount.builder()
                .setAmount(0L)
                .setCurrency("usd")
                .build()
            )
            .setDisplayName("Free shipping")
            .setDeliveryEstimate(
              SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.builder()
                .setMinimum(
                  SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.builder()
                    .setUnit(
                      SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.Unit.BUSINESS_DAY
                    )
                    .setValue(5L)
                    .build()
                )
                .setMaximum(
                  SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.builder()
                    .setUnit(
                      SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.Unit.BUSINESS_DAY
                    )
                    .setValue(7L)
                    .build()
                )
                .build()
            )
            .build()
        )
        .build()
    )
    .addShippingOption(
      SessionCreateParams.ShippingOption.builder()
        .setShippingRateData(
          SessionCreateParams.ShippingOption.ShippingRateData.builder()
            .setType(
              SessionCreateParams.ShippingOption.ShippingRateData.Type.FIXED_AMOUNT
            )
            .setFixedAmount(
              SessionCreateParams.ShippingOption.ShippingRateData.FixedAmount.builder()
                .setAmount(1500L)
                .setCurrency("usd")
                .build()
            )
            .setDisplayName("Next day air")
            .setDeliveryEstimate(
              SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.builder()
                .setMinimum(
                  SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.builder()
                    .setUnit(
                      SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.Unit.BUSINESS_DAY
                    )
                    .setValue(1L)
                    .build()
                )
                .setMaximum(
                  SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.builder()
                    .setUnit(
                      SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.Unit.BUSINESS_DAY
                    )
                    .setValue(1L)
                    .build()
                )
                .build()
            )
            .build()
        )
        .build()
    )
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setCurrency("usd")
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setUnitAmount(2000L)
            .build()
        )
        .setQuantity(1L)
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
  shipping_address_collection: {
    allowed_countries: ['US', 'CA'],
  },
  shipping_options: [
    {
      shipping_rate_data: {
        type: 'fixed_amount',
        fixed_amount: {
          amount: 0,
          currency: 'usd',
        },
        display_name: 'Free shipping',
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
      },
    },
    {
      shipping_rate_data: {
        type: 'fixed_amount',
        fixed_amount: {
          amount: 1500,
          currency: 'usd',
        },
        display_name: 'Next day air',
        delivery_estimate: {
          minimum: {
            unit: 'business_day',
            value: 1,
          },
          maximum: {
            unit: 'business_day',
            value: 1,
          },
        },
      },
    },
  ],
  line_items: [
    {
      price_data: {
        currency: 'usd',
        product_data: {
          name: 'T-shirt',
        },
        unit_amount: 2000,
      },
      quantity: 1,
    },
  ],
  mode: 'payment',
  success_url: 'https://example.com/success',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CheckoutSessionCreateParams{
  ShippingAddressCollection: &stripe.CheckoutSessionCreateShippingAddressCollectionParams{
    AllowedCountries: []*string{stripe.String("US"), stripe.String("CA")},
  },
  ShippingOptions: []*stripe.CheckoutSessionCreateShippingOptionParams{
    &stripe.CheckoutSessionCreateShippingOptionParams{
      ShippingRateData: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataParams{
        Type: stripe.String("fixed_amount"),
        FixedAmount: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataFixedAmountParams{
          Amount: stripe.Int64(0),
          Currency: stripe.String(stripe.CurrencyUSD),
        },
        DisplayName: stripe.String("Free shipping"),
        DeliveryEstimate: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataDeliveryEstimateParams{
          Minimum: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataDeliveryEstimateMinimumParams{
            Unit: stripe.String("business_day"),
            Value: stripe.Int64(5),
          },
          Maximum: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataDeliveryEstimateMaximumParams{
            Unit: stripe.String("business_day"),
            Value: stripe.Int64(7),
          },
        },
      },
    },
    &stripe.CheckoutSessionCreateShippingOptionParams{
      ShippingRateData: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataParams{
        Type: stripe.String("fixed_amount"),
        FixedAmount: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataFixedAmountParams{
          Amount: stripe.Int64(1500),
          Currency: stripe.String(stripe.CurrencyUSD),
        },
        DisplayName: stripe.String("Next day air"),
        DeliveryEstimate: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataDeliveryEstimateParams{
          Minimum: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataDeliveryEstimateMinimumParams{
            Unit: stripe.String("business_day"),
            Value: stripe.Int64(1),
          },
          Maximum: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataDeliveryEstimateMaximumParams{
            Unit: stripe.String("business_day"),
            Value: stripe.Int64(1),
          },
        },
      },
    },
  },
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        Currency: stripe.String(stripe.CurrencyUSD),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        UnitAmount: stripe.Int64(2000),
      },
      Quantity: stripe.Int64(1),
    },
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
    ShippingAddressCollection = new Stripe.Checkout.SessionShippingAddressCollectionOptions
    {
        AllowedCountries = new List<string> { "US", "CA" },
    },
    ShippingOptions = new List<Stripe.Checkout.SessionShippingOptionOptions>
    {
        new Stripe.Checkout.SessionShippingOptionOptions
        {
            ShippingRateData = new Stripe.Checkout.SessionShippingOptionShippingRateDataOptions
            {
                Type = "fixed_amount",
                FixedAmount = new Stripe.Checkout.SessionShippingOptionShippingRateDataFixedAmountOptions
                {
                    Amount = 0,
                    Currency = "usd",
                },
                DisplayName = "Free shipping",
                DeliveryEstimate = new Stripe.Checkout.SessionShippingOptionShippingRateDataDeliveryEstimateOptions
                {
                    Minimum = new Stripe.Checkout.SessionShippingOptionShippingRateDataDeliveryEstimateMinimumOptions
                    {
                        Unit = "business_day",
                        Value = 5,
                    },
                    Maximum = new Stripe.Checkout.SessionShippingOptionShippingRateDataDeliveryEstimateMaximumOptions
                    {
                        Unit = "business_day",
                        Value = 7,
                    },
                },
            },
        },
        new Stripe.Checkout.SessionShippingOptionOptions
        {
            ShippingRateData = new Stripe.Checkout.SessionShippingOptionShippingRateDataOptions
            {
                Type = "fixed_amount",
                FixedAmount = new Stripe.Checkout.SessionShippingOptionShippingRateDataFixedAmountOptions
                {
                    Amount = 1500,
                    Currency = "usd",
                },
                DisplayName = "Next day air",
                DeliveryEstimate = new Stripe.Checkout.SessionShippingOptionShippingRateDataDeliveryEstimateOptions
                {
                    Minimum = new Stripe.Checkout.SessionShippingOptionShippingRateDataDeliveryEstimateMinimumOptions
                    {
                        Unit = "business_day",
                        Value = 1,
                    },
                    Maximum = new Stripe.Checkout.SessionShippingOptionShippingRateDataDeliveryEstimateMaximumOptions
                    {
                        Unit = "business_day",
                        Value = 1,
                    },
                },
            },
        },
    },
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                Currency = "usd",
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                UnitAmount = 2000,
            },
            Quantity = 1,
        },
    },
    Mode = "payment",
    SuccessUrl = "https://example.com/success",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

#### Embedded form

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d billing_address_collection=required \
  -d "shipping_address_collection[allowed_countries][0]"=US \
  -d "shipping_address_collection[allowed_countries][1]"=CA \
  -d "shipping_options[0][shipping_rate_data][type]"=fixed_amount \
  -d "shipping_options[0][shipping_rate_data][fixed_amount][amount]"=0 \
  -d "shipping_options[0][shipping_rate_data][fixed_amount][currency]"=usd \
  -d "shipping_options[0][shipping_rate_data][display_name]"="Free shipping" \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][minimum][unit]"=business_day \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][minimum][value]"=5 \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][maximum][unit]"=business_day \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][maximum][value]"=7 \
  -d "shipping_options[1][shipping_rate_data][type]"=fixed_amount \
  -d "shipping_options[1][shipping_rate_data][fixed_amount][amount]"=1500 \
  -d "shipping_options[1][shipping_rate_data][fixed_amount][currency]"=usd \
  -d "shipping_options[1][shipping_rate_data][display_name]"="Next day air" \
  -d "shipping_options[1][shipping_rate_data][delivery_estimate][minimum][unit]"=business_day \
  -d "shipping_options[1][shipping_rate_data][delivery_estimate][minimum][value]"=1 \
  -d "shipping_options[1][shipping_rate_data][delivery_estimate][maximum][unit]"=business_day \
  -d "shipping_options[1][shipping_rate_data][delivery_estimate][maximum][value]"=1 \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][unit_amount]"=2000 \
  -d "line_items[0][quantity]"=1 \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  --billing-address-collection=required \
  -d "shipping_address_collection[allowed_countries][0]"=US \
  -d "shipping_address_collection[allowed_countries][1]"=CA \
  -d "shipping_options[0][shipping_rate_data][type]"=fixed_amount \
  -d "shipping_options[0][shipping_rate_data][fixed_amount][amount]"=0 \
  -d "shipping_options[0][shipping_rate_data][fixed_amount][currency]"=usd \
  -d "shipping_options[0][shipping_rate_data][display_name]"="Free shipping" \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][minimum][unit]"=business_day \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][minimum][value]"=5 \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][maximum][unit]"=business_day \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][maximum][value]"=7 \
  -d "shipping_options[1][shipping_rate_data][type]"=fixed_amount \
  -d "shipping_options[1][shipping_rate_data][fixed_amount][amount]"=1500 \
  -d "shipping_options[1][shipping_rate_data][fixed_amount][currency]"=usd \
  -d "shipping_options[1][shipping_rate_data][display_name]"="Next day air" \
  -d "shipping_options[1][shipping_rate_data][delivery_estimate][minimum][unit]"=business_day \
  -d "shipping_options[1][shipping_rate_data][delivery_estimate][minimum][value]"=1 \
  -d "shipping_options[1][shipping_rate_data][delivery_estimate][maximum][unit]"=business_day \
  -d "shipping_options[1][shipping_rate_data][delivery_estimate][maximum][value]"=1 \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][unit_amount]"=2000 \
  -d "line_items[0][quantity]"=1 \
  --mode=payment \
  --ui-mode=embedded \
  --return-url="https://example.com/return"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  billing_address_collection: 'required',
  shipping_address_collection: {allowed_countries: ['US', 'CA']},
  shipping_options: [
    {
      shipping_rate_data: {
        type: 'fixed_amount',
        fixed_amount: {
          amount: 0,
          currency: 'usd',
        },
        display_name: 'Free shipping',
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
      },
    },
    {
      shipping_rate_data: {
        type: 'fixed_amount',
        fixed_amount: {
          amount: 1500,
          currency: 'usd',
        },
        display_name: 'Next day air',
        delivery_estimate: {
          minimum: {
            unit: 'business_day',
            value: 1,
          },
          maximum: {
            unit: 'business_day',
            value: 1,
          },
        },
      },
    },
  ],
  line_items: [
    {
      price_data: {
        currency: 'usd',
        product_data: {name: 'T-shirt'},
        unit_amount: 2000,
      },
      quantity: 1,
    },
  ],
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
  "billing_address_collection": "required",
  "shipping_address_collection": {"allowed_countries": ["US", "CA"]},
  "shipping_options": [
    {
      "shipping_rate_data": {
        "type": "fixed_amount",
        "fixed_amount": {"amount": 0, "currency": "usd"},
        "display_name": "Free shipping",
        "delivery_estimate": {
          "minimum": {"unit": "business_day", "value": 5},
          "maximum": {"unit": "business_day", "value": 7},
        },
      },
    },
    {
      "shipping_rate_data": {
        "type": "fixed_amount",
        "fixed_amount": {"amount": 1500, "currency": "usd"},
        "display_name": "Next day air",
        "delivery_estimate": {
          "minimum": {"unit": "business_day", "value": 1},
          "maximum": {"unit": "business_day", "value": 1},
        },
      },
    },
  ],
  "line_items": [
    {
      "price_data": {
        "currency": "usd",
        "product_data": {"name": "T-shirt"},
        "unit_amount": 2000,
      },
      "quantity": 1,
    },
  ],
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
  'billing_address_collection' => 'required',
  'shipping_address_collection' => ['allowed_countries' => ['US', 'CA']],
  'shipping_options' => [
    [
      'shipping_rate_data' => [
        'type' => 'fixed_amount',
        'fixed_amount' => [
          'amount' => 0,
          'currency' => 'usd',
        ],
        'display_name' => 'Free shipping',
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
      ],
    ],
    [
      'shipping_rate_data' => [
        'type' => 'fixed_amount',
        'fixed_amount' => [
          'amount' => 1500,
          'currency' => 'usd',
        ],
        'display_name' => 'Next day air',
        'delivery_estimate' => [
          'minimum' => [
            'unit' => 'business_day',
            'value' => 1,
          ],
          'maximum' => [
            'unit' => 'business_day',
            'value' => 1,
          ],
        ],
      ],
    ],
  ],
  'line_items' => [
    [
      'price_data' => [
        'currency' => 'usd',
        'product_data' => ['name' => 'T-shirt'],
        'unit_amount' => 2000,
      ],
      'quantity' => 1,
    ],
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
    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
    .setShippingAddressCollection(
      SessionCreateParams.ShippingAddressCollection.builder()
        .addAllowedCountry(
          SessionCreateParams.ShippingAddressCollection.AllowedCountry.US
        )
        .addAllowedCountry(
          SessionCreateParams.ShippingAddressCollection.AllowedCountry.CA
        )
        .build()
    )
    .addShippingOption(
      SessionCreateParams.ShippingOption.builder()
        .setShippingRateData(
          SessionCreateParams.ShippingOption.ShippingRateData.builder()
            .setType(
              SessionCreateParams.ShippingOption.ShippingRateData.Type.FIXED_AMOUNT
            )
            .setFixedAmount(
              SessionCreateParams.ShippingOption.ShippingRateData.FixedAmount.builder()
                .setAmount(0L)
                .setCurrency("usd")
                .build()
            )
            .setDisplayName("Free shipping")
            .setDeliveryEstimate(
              SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.builder()
                .setMinimum(
                  SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.builder()
                    .setUnit(
                      SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.Unit.BUSINESS_DAY
                    )
                    .setValue(5L)
                    .build()
                )
                .setMaximum(
                  SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.builder()
                    .setUnit(
                      SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.Unit.BUSINESS_DAY
                    )
                    .setValue(7L)
                    .build()
                )
                .build()
            )
            .build()
        )
        .build()
    )
    .addShippingOption(
      SessionCreateParams.ShippingOption.builder()
        .setShippingRateData(
          SessionCreateParams.ShippingOption.ShippingRateData.builder()
            .setType(
              SessionCreateParams.ShippingOption.ShippingRateData.Type.FIXED_AMOUNT
            )
            .setFixedAmount(
              SessionCreateParams.ShippingOption.ShippingRateData.FixedAmount.builder()
                .setAmount(1500L)
                .setCurrency("usd")
                .build()
            )
            .setDisplayName("Next day air")
            .setDeliveryEstimate(
              SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.builder()
                .setMinimum(
                  SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.builder()
                    .setUnit(
                      SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.Unit.BUSINESS_DAY
                    )
                    .setValue(1L)
                    .build()
                )
                .setMaximum(
                  SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.builder()
                    .setUnit(
                      SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.Unit.BUSINESS_DAY
                    )
                    .setValue(1L)
                    .build()
                )
                .build()
            )
            .build()
        )
        .build()
    )
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setCurrency("usd")
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setUnitAmount(2000L)
            .build()
        )
        .setQuantity(1L)
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
  billing_address_collection: 'required',
  shipping_address_collection: {
    allowed_countries: ['US', 'CA'],
  },
  shipping_options: [
    {
      shipping_rate_data: {
        type: 'fixed_amount',
        fixed_amount: {
          amount: 0,
          currency: 'usd',
        },
        display_name: 'Free shipping',
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
      },
    },
    {
      shipping_rate_data: {
        type: 'fixed_amount',
        fixed_amount: {
          amount: 1500,
          currency: 'usd',
        },
        display_name: 'Next day air',
        delivery_estimate: {
          minimum: {
            unit: 'business_day',
            value: 1,
          },
          maximum: {
            unit: 'business_day',
            value: 1,
          },
        },
      },
    },
  ],
  line_items: [
    {
      price_data: {
        currency: 'usd',
        product_data: {
          name: 'T-shirt',
        },
        unit_amount: 2000,
      },
      quantity: 1,
    },
  ],
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
  BillingAddressCollection: stripe.String(stripe.CheckoutSessionBillingAddressCollectionRequired),
  ShippingAddressCollection: &stripe.CheckoutSessionCreateShippingAddressCollectionParams{
    AllowedCountries: []*string{stripe.String("US"), stripe.String("CA")},
  },
  ShippingOptions: []*stripe.CheckoutSessionCreateShippingOptionParams{
    &stripe.CheckoutSessionCreateShippingOptionParams{
      ShippingRateData: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataParams{
        Type: stripe.String("fixed_amount"),
        FixedAmount: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataFixedAmountParams{
          Amount: stripe.Int64(0),
          Currency: stripe.String(stripe.CurrencyUSD),
        },
        DisplayName: stripe.String("Free shipping"),
        DeliveryEstimate: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataDeliveryEstimateParams{
          Minimum: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataDeliveryEstimateMinimumParams{
            Unit: stripe.String("business_day"),
            Value: stripe.Int64(5),
          },
          Maximum: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataDeliveryEstimateMaximumParams{
            Unit: stripe.String("business_day"),
            Value: stripe.Int64(7),
          },
        },
      },
    },
    &stripe.CheckoutSessionCreateShippingOptionParams{
      ShippingRateData: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataParams{
        Type: stripe.String("fixed_amount"),
        FixedAmount: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataFixedAmountParams{
          Amount: stripe.Int64(1500),
          Currency: stripe.String(stripe.CurrencyUSD),
        },
        DisplayName: stripe.String("Next day air"),
        DeliveryEstimate: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataDeliveryEstimateParams{
          Minimum: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataDeliveryEstimateMinimumParams{
            Unit: stripe.String("business_day"),
            Value: stripe.Int64(1),
          },
          Maximum: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataDeliveryEstimateMaximumParams{
            Unit: stripe.String("business_day"),
            Value: stripe.Int64(1),
          },
        },
      },
    },
  },
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        Currency: stripe.String(stripe.CurrencyUSD),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        UnitAmount: stripe.Int64(2000),
      },
      Quantity: stripe.Int64(1),
    },
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
    BillingAddressCollection = "required",
    ShippingAddressCollection = new Stripe.Checkout.SessionShippingAddressCollectionOptions
    {
        AllowedCountries = new List<string> { "US", "CA" },
    },
    ShippingOptions = new List<Stripe.Checkout.SessionShippingOptionOptions>
    {
        new Stripe.Checkout.SessionShippingOptionOptions
        {
            ShippingRateData = new Stripe.Checkout.SessionShippingOptionShippingRateDataOptions
            {
                Type = "fixed_amount",
                FixedAmount = new Stripe.Checkout.SessionShippingOptionShippingRateDataFixedAmountOptions
                {
                    Amount = 0,
                    Currency = "usd",
                },
                DisplayName = "Free shipping",
                DeliveryEstimate = new Stripe.Checkout.SessionShippingOptionShippingRateDataDeliveryEstimateOptions
                {
                    Minimum = new Stripe.Checkout.SessionShippingOptionShippingRateDataDeliveryEstimateMinimumOptions
                    {
                        Unit = "business_day",
                        Value = 5,
                    },
                    Maximum = new Stripe.Checkout.SessionShippingOptionShippingRateDataDeliveryEstimateMaximumOptions
                    {
                        Unit = "business_day",
                        Value = 7,
                    },
                },
            },
        },
        new Stripe.Checkout.SessionShippingOptionOptions
        {
            ShippingRateData = new Stripe.Checkout.SessionShippingOptionShippingRateDataOptions
            {
                Type = "fixed_amount",
                FixedAmount = new Stripe.Checkout.SessionShippingOptionShippingRateDataFixedAmountOptions
                {
                    Amount = 1500,
                    Currency = "usd",
                },
                DisplayName = "Next day air",
                DeliveryEstimate = new Stripe.Checkout.SessionShippingOptionShippingRateDataDeliveryEstimateOptions
                {
                    Minimum = new Stripe.Checkout.SessionShippingOptionShippingRateDataDeliveryEstimateMinimumOptions
                    {
                        Unit = "business_day",
                        Value = 1,
                    },
                    Maximum = new Stripe.Checkout.SessionShippingOptionShippingRateDataDeliveryEstimateMaximumOptions
                    {
                        Unit = "business_day",
                        Value = 1,
                    },
                },
            },
        },
    },
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                Currency = "usd",
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                UnitAmount = 2000,
            },
            Quantity = 1,
        },
    },
    Mode = "payment",
    UiMode = "embedded",
    ReturnUrl = "https://example.com/return",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Checkout.Sessions;
Stripe.Checkout.Session session = service.Create(options);
```

If successful, the shipping selector appears in your checkout flow:
![The shipping selector in the checkout flow](https://b.stripecdn.com/docs-statics-srv/assets/example-checkout-session.5807984bdc0a25ddb53aab00768dd079.jpg)

The shipping selector in the checkout flow

## Optional: Handle completed transactions

After the payment succeeds, you can retrieve the shipping amount in the [amount_total](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-amount_total) attribute of the [shipping_cost](https://docs.stripe.com/api/checkout/sessions/object.md#checkout_session_object-shipping_cost). You can also retrieve the selected shipping rate using the `shipping_rate` attribute in `shipping_cost`. To access the `shipping_cost` property, you must [create an event handler](https://docs.stripe.com/checkout/fulfillment.md#create-payment-event-handler) to handle completed Checkout Sessions. You can test a handler by [installing the Stripe CLI](https://docs.stripe.com/stripe-cli.md) and using `stripe listen --forward-to localhost:4242/webhook` to [forward events to your local server](https://docs.stripe.com/webhooks.md#test-webhook). In the following code sample, the handler allows for the user to access the `shipping_property`:

#### Ruby

```ruby
# Set your secret key. Remember to switch to your live secret key in production!
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = "<<YOUR_SECRET_KEY>>"

require 'sinatra'

# You can find your endpoint's secret in your webhook settings
endpoint_secret = 'whsec_...'

post '/webhook' do
  event = nil

  # Verify webhook signature and extract the event
  # See https://stripe.com/docs/webhooks#verify-events for more information.
  begin
    sig_header = request.env['HTTP_STRIPE_SIGNATURE']
    payload = request.body.read
    event = Stripe::Webhook.construct_event(payload, sig_header, endpoint_secret)
  rescue JSON::ParserError => e
    # Invalid payload
    return status 400
  rescue Stripe::SignatureVerificationError => e
    # Invalid signature
    return status 400
  end

  if event['type'] == 'checkout.session.completed'
    checkout_session = event['data']['object']

    fulfill_order(checkout_session)
  end

  status 200
end

def fulfill_order(checkout_session)selected_shipping_rate = Stripe::ShippingRate.retrieve(checkout_session.shipping_cost.shipping_rate)
  shipping_total = checkout_session.shipping_cost.amount_total

  # TODO: Remove error and implement...
  raise NotImplementedError.new(<<~MSG)
    Given the Checkout Session "#{checkout_session.id}" load your internal order from the database then implement your own fulfillment logic.
  MSG
end
```

#### Python

```python
import stripe

# Using Django
from django.http import HttpResponse

# Set your secret key. Remember to switch to your live secret key in production!
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

# You can find your endpoint's secret in your webhook settings
endpoint_secret = 'whsec_...'

@csrf_exempt
def my_webhook_view(request):
  payload = request.body
  sig_header = request.META['HTTP_STRIPE_SIGNATURE']
  event = None

  try:
    event = stripe.Webhook.construct_event(
      payload, sig_header, endpoint_secret
    )
  except ValueError as e:
    # Invalid payload
    return HttpResponse(status=400)
  except stripe.error.SignatureVerificationError as e:
    # Invalid signature
    return HttpResponse(status=400)

  # Handle the checkout.session.completed event
  if event['type'] == 'checkout.session.completed':
    session = event['data']['object']

    # Fulfill the purchase...
    fulfill_order(session)

  # Passed signature verification
  return HttpResponse(status=200)

def fulfill_order(session):selected_shipping_rate = stripe.ShippingRate.retrieve(checkout_session.shipping_cost.shipping_rate)
  shipping_total = session.shipping_cost.amount_total

  # TODO: Remove error and implement...
  raise NotImplementedError("Given the Checkout Session \"" + session['id'] +  "\", load your internal order from the database then implement your own fulfillment logic.")
```

#### PHP

```php
// Set your secret key. Remember to switch to your live secret key in production!
// See your keys here: https://dashboard.stripe.com/apikeys
\Stripe\Stripe::setApiKey('<<YOUR_SECRET_KEY>>');

$stripe = new \Stripe\StripeClient('{$ key type="secret" %}');

// You can find your endpoint's secret in your webhook settings
$endpoint_secret = 'whsec_...';

$payload = @file_get_contents('php://input');
$sig_header = $_SERVER['HTTP_STRIPE_SIGNATURE'];
$event = null;

try {
  $event = \Stripe\Webhook::constructEvent(
    $payload, $sig_header, $endpoint_secret
  );
} catch(\UnexpectedValueException $e) {
  // Invalid payload
  http_response_code(400);
  exit();
} catch(\Stripe\Exception\SignatureVerificationException $e) {
  // Invalid signature
  http_response_code(400);
  exit();
}

function fulfill_order($session) {$selected_shipping_rate = $stripe->shippingRates->retrieve(
    $session->shipping_cost->shipping_rate,
    []
  );

  $shipping_total = $session->shipping_cost->amount_total;

  // TODO: Remove error and implement...
  throw new Exception("Given the Checkout Session $session->id, load your internal order from the database then implement your own fulfillment logic.")
}

// Handle the checkout.session.completed event
if ($event->type == 'checkout.session.completed') {
  $session = $event->data->object;

  // Fulfill the purchase...
  fulfill_order($session);
}

http_response_code(200);
```

#### Java

```java
// Set your secret key. Remember to switch to your live secret key in production!
// See your keys here: https://dashboard.stripe.com/apikeys
Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

// You can find your endpoint's secret in your webhook settings
String endpointSecret = "whsec_...";

public void fulfillOrder(Session session) {Long shippingTotal = checkoutSession
    .getRawJsonObject()
    .getAsJsonObject("shipping_cost")
    .getAsJsonPrimitive("amount_total")
    .getAsLong();

  String error = String.format("Given the Checkout Session \"%s\" load your internal order from the database then implement your own fulfillment logic.", session.getId());
  throw new UnsupportedOperationException(error);
}

// Using the Spark framework
public Object handle(Request request, Response response) {
  String payload = request.body();
  String sigHeader = request.headers("Stripe-Signature");
  Event event = null;

  try {
    event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
  } catch (JsonSyntaxException e) {
    // Invalid payload
    response.status(400);
    return "";
  } catch (SignatureVerificationException e) {
    // Invalid signature
    response.status(400);
    return "";
  }

  // Handle the checkout.session.completed event
  if ("checkout.session.completed".equals(event.getType())) {
    Session session = (Session) event.getDataObjectDeserializer().getObject();

    Long shippingTotal = session.getShippingCost().getAmountTotal();
    ShippingRate selectedShippingRate = session.getShippingCost().getShippingRateObject();

    // Fulfill the purchase...
    fulfillOrder(session);
  }

  response.status(200);
  return "";
}
```

#### Node.js

```javascript
// Set your secret key. Remember to switch to your live secret key in production!
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

// Find your endpoint's secret in your Dashboard's webhook settings
const endpointSecret = 'whsec_...';

// Using Express
const app = require('express')();

// Use body-parser to retrieve the raw body as a buffer
const bodyParser = require('body-parser');

const fulfillOrder = (session) => {
const selectedShippingRate = await stripe.shippingRates.retrieve(session.shipping_cost.shipping_rate);
  const shippingTotal = session.shipping_cost.amount_total;

  // TODO: Remove error and implement...
  throw new Error(`
    Given the Checkout Session ${session.id}, load your internal order from the database then implement your own fulfillment logic.`
  );
}

app.post('/webhook', bodyParser.raw({type: 'application/json'}), (request, response) => {
  const payload = request.body;
  const sig = request.headers['stripe-signature'];

  let event;

  try {
    event = stripe.webhooks.constructEvent(payload, sig, endpointSecret);
  } catch (err) {
    return response.status(400).send(`Webhook Error: ${err.message}`);
  }

  // Handle the checkout.session.completed event
  if (event.type === 'checkout.session.completed') {
    const session = event.data.object;

    // Fulfill the purchase...
    fulfillOrder(session);
  }

  response.status(200).end();
});

app.listen(4242, () => console.log('Running on port 4242'));
```

#### Go

```go
// Set your secret key. Remember to switch to your live secret key in production!
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

func FulfillOrder(session stripe.CheckoutSession) {var rawData map[string]interface{}
  _ = json.Unmarshal(session.LastResponse.RawJSON, &rawData)

  shippingCost, ok := rawData["shipping_cost"].(map[string]interface{})
  if ok {
    shippingTotal := shippingCost["amount_total"].(float64)

    // TODO: Remove error and implement...
    return fmt.Errorf("given the Checkout Session %q, load your internal order from the database then implement your own fulfillment logic.", session.Id)
  }
}

http.HandleFunc("/webhook", func(w http.ResponseWriter, req *http.Request) {
  const MaxBodyBytes = int64(65536)
  req.Body = http.MaxBytesReader(w, req.Body, MaxBodyBytes)

  body, err := ioutil.ReadAll(req.Body)
  if err != nil {
    fmt.Fprintf(os.Stderr, "Error reading request body: %v\n", err)
    w.WriteHeader(http.StatusServiceUnavailable)
    return
  }

  // Pass the request body and Stripe-Signature header to ConstructEvent, along with the webhook signing key
  // You can find your endpoint's secret in your webhook settings
  endpointSecret := "whsec_...";
  event, err := webhook.ConstructEvent(body, req.Header.Get("Stripe-Signature"), endpointSecret)

  if err != nil {
    fmt.Fprintf(os.Stderr, "Error verifying webhook signature: %v\n", err)
    w.WriteHeader(http.StatusBadRequest) // Return a 400 error on a bad signature
    return
  }

  // Handle the checkout.session.completed event
  if event.Type == "checkout.session.completed" {
    var session stripe.CheckoutSession
    err := json.Unmarshal(event.Data.Raw, &session)
    if err != nil {
      fmt.Fprintf(os.Stderr, "Error parsing webhook JSON: %v\n", err)
      w.WriteHeader(http.StatusBadRequest)
      return
    }

    // Fulfill the purchase...
    FulfillOrder(session)
  }

  w.WriteHeader(http.StatusOK)
})
```

#### .NET

```dotnet
using System;
using System.IO;
using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;
using Stripe;
using Stripe.Checkout;

// Set your secret key. Remember to switch to your live secret key in production!
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

namespace workspace.Controllers
{
  [Route("api/[controller]")]
  public class StripeWebHook : Controller
  {
    // You can find your endpoint's secret in your webhook settings
    const string secret = "whsec_...";

    [HttpPost]
    public async Task<IActionResult> Index()
    {
      var json = await new StreamReader(HttpContext.Request.Body).ReadToEndAsync();

      try
      {
        var stripeEvent = EventUtility.ConstructEvent(
          json,
          Request.Headers["Stripe-Signature"],
          secret
        );

        // Handle the checkout.session.completed event
        // If on SDK version < 46, use class Events instead of EventTypes
        if (stripeEvent.Type == EventTypes.CheckoutSessionCompleted)
        {
          var session = stripeEvent.Data.Object as Checkout.Session;

          // Fulfill the purchase...
          this.FulfillOrder(session);
        }

        return Ok();
      }
      catch (StripeException e)
      {
        return BadRequest();
      }
    }

    private void FulfillOrder(Checkout.Session session) {var shippingAmount = session.ShippingCost.AmountTotal;

      // TODO: Remove error and implement...
      throw new NotImplementedException($"Given the Checkout Session \"{session.Id}\" load your internal order from the database then implement your own fulfillment logic.");
    }
  }
}
```

## Optional: Define a delivery estimate

You can configure shipping rates using a number of delivery estimate combinations. The following table contains some examples of plain English delivery estimates, and their corresponding `delivery_estimate.minimum` and `delivery_estimate.maximum` values:

| Delivery Estimate          | Minimum                                                      | Maximum                                                      |
| -------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 1 day                      | ```es6
  {
    unit: 'day',
    value: 1,
  }
  ```          | ```es6
  {
    unit: 'day',
    value: 1,
  }
  ```          |
| 1 business day             | ```es6
  {
    unit: 'business_day',
    value: 1,
  }
  ``` | ```es6
  {
    unit: 'business_day',
    value: 1,
  }
  ``` |
| At least 2 business days   | ```es6
  {
    unit: 'business_day',
    value: 2,
  }
  ``` | ```es6
  null
  ```                                          |
| 3 to 7 days                | ```es6
  {
    unit: 'day',
    value: 3,
  }
  ```          | ```es6
  {
    unit: 'day',
    value: 7,
  }
  ```          |
| 4 to 8 hours               | ```es6
  {
    unit: 'hour',
    value: 4,
  }
  ```         | ```es6
  {
    unit: 'hour',
    value: 8,
  }
  ```         |
| 4 hours to 2 business days | ```es6
  {
    unit: 'hour',
    value: 4,
  }
  
  ```      | ```es6
  {
    unit: 'business_day',
    value: 2,
  }
  ``` |

## Optional: Charge tax for shipping

You can use [Stripe Tax](https://docs.stripe.com/tax/checkout.md) to automatically calculate tax on shipping fees by setting a `tax_code` and `tax_behavior` on your shipping rate. Stripe Tax automatically determines whether shipping is taxable ([as taxability varies by state and country](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md#shipping-tax-code)) and applies the correct tax rate if so.

When creating a shipping rate with `shipping_rate_data` or through [Create a Shipping Rate](https://docs.stripe.com/api/shipping_rates/create.md), you can add a `tax_behavior` and `tax_code` parameter to the shipping rate.

We recommend setting the `tax_code` to `Shipping` (`txcd_92010001`) to make sure that you always charge the correct tax. You can also set the shipping rate `tax_code` to `Nontaxable` (`txcd_00000000`) if you don’t want to charge tax.

For this example, we set the `tax_behavior` to `exclusive`, which is common in the US. Learn more about [tax behavior](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md#tax-behavior).

#### Stripe-hosted page

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d billing_address_collection=required \
  -d "shipping_address_collection[allowed_countries][0]"=US \
  -d "shipping_address_collection[allowed_countries][1]"=CA \
  -d "shipping_options[0][shipping_rate_data][type]"=fixed_amount \
  -d "shipping_options[0][shipping_rate_data][fixed_amount][amount]"=0 \
  -d "shipping_options[0][shipping_rate_data][fixed_amount][currency]"=usd \
  -d "shipping_options[0][shipping_rate_data][display_name]"="Free shipping" \
  -d "shipping_options[0][shipping_rate_data][tax_behavior]"=exclusive \
  -d "shipping_options[0][shipping_rate_data][tax_code]"=txcd_92010001 \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][minimum][unit]"=business_day \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][minimum][value]"=5 \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][maximum][unit]"=business_day \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][maximum][value]"=7 \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][unit_amount]"=2000 \
  -d "line_items[0][price_data][tax_behavior]"=exclusive \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d mode=payment \
  --data-urlencode success_url="https://example.com/success"
```

```cli
stripe checkout sessions create  \
  --billing-address-collection=required \
  -d "shipping_address_collection[allowed_countries][0]"=US \
  -d "shipping_address_collection[allowed_countries][1]"=CA \
  -d "shipping_options[0][shipping_rate_data][type]"=fixed_amount \
  -d "shipping_options[0][shipping_rate_data][fixed_amount][amount]"=0 \
  -d "shipping_options[0][shipping_rate_data][fixed_amount][currency]"=usd \
  -d "shipping_options[0][shipping_rate_data][display_name]"="Free shipping" \
  -d "shipping_options[0][shipping_rate_data][tax_behavior]"=exclusive \
  -d "shipping_options[0][shipping_rate_data][tax_code]"=txcd_92010001 \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][minimum][unit]"=business_day \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][minimum][value]"=5 \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][maximum][unit]"=business_day \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][maximum][value]"=7 \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][unit_amount]"=2000 \
  -d "line_items[0][price_data][tax_behavior]"=exclusive \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  --mode=payment \
  --success-url="https://example.com/success"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

session = client.v1.checkout.sessions.create({
  billing_address_collection: 'required',
  shipping_address_collection: {allowed_countries: ['US', 'CA']},
  shipping_options: [
    {
      shipping_rate_data: {
        type: 'fixed_amount',
        fixed_amount: {
          amount: 0,
          currency: 'usd',
        },
        display_name: 'Free shipping',
        tax_behavior: 'exclusive',
        tax_code: 'txcd_92010001',
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
      },
    },
  ],
  line_items: [
    {
      price_data: {
        currency: 'usd',
        product_data: {name: 'T-shirt'},
        unit_amount: 2000,
        tax_behavior: 'exclusive',
      },
      quantity: 1,
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
  "billing_address_collection": "required",
  "shipping_address_collection": {"allowed_countries": ["US", "CA"]},
  "shipping_options": [
    {
      "shipping_rate_data": {
        "type": "fixed_amount",
        "fixed_amount": {"amount": 0, "currency": "usd"},
        "display_name": "Free shipping",
        "tax_behavior": "exclusive",
        "tax_code": "txcd_92010001",
        "delivery_estimate": {
          "minimum": {"unit": "business_day", "value": 5},
          "maximum": {"unit": "business_day", "value": 7},
        },
      },
    },
  ],
  "line_items": [
    {
      "price_data": {
        "currency": "usd",
        "product_data": {"name": "T-shirt"},
        "unit_amount": 2000,
        "tax_behavior": "exclusive",
      },
      "quantity": 1,
    },
  ],
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
  'billing_address_collection' => 'required',
  'shipping_address_collection' => ['allowed_countries' => ['US', 'CA']],
  'shipping_options' => [
    [
      'shipping_rate_data' => [
        'type' => 'fixed_amount',
        'fixed_amount' => [
          'amount' => 0,
          'currency' => 'usd',
        ],
        'display_name' => 'Free shipping',
        'tax_behavior' => 'exclusive',
        'tax_code' => 'txcd_92010001',
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
      ],
    ],
  ],
  'line_items' => [
    [
      'price_data' => [
        'currency' => 'usd',
        'product_data' => ['name' => 'T-shirt'],
        'unit_amount' => 2000,
        'tax_behavior' => 'exclusive',
      ],
      'quantity' => 1,
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
    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
    .setShippingAddressCollection(
      SessionCreateParams.ShippingAddressCollection.builder()
        .addAllowedCountry(
          SessionCreateParams.ShippingAddressCollection.AllowedCountry.US
        )
        .addAllowedCountry(
          SessionCreateParams.ShippingAddressCollection.AllowedCountry.CA
        )
        .build()
    )
    .addShippingOption(
      SessionCreateParams.ShippingOption.builder()
        .setShippingRateData(
          SessionCreateParams.ShippingOption.ShippingRateData.builder()
            .setType(
              SessionCreateParams.ShippingOption.ShippingRateData.Type.FIXED_AMOUNT
            )
            .setFixedAmount(
              SessionCreateParams.ShippingOption.ShippingRateData.FixedAmount.builder()
                .setAmount(0L)
                .setCurrency("usd")
                .build()
            )
            .setDisplayName("Free shipping")
            .setTaxBehavior(
              SessionCreateParams.ShippingOption.ShippingRateData.TaxBehavior.EXCLUSIVE
            )
            .setTaxCode("txcd_92010001")
            .setDeliveryEstimate(
              SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.builder()
                .setMinimum(
                  SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.builder()
                    .setUnit(
                      SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.Unit.BUSINESS_DAY
                    )
                    .setValue(5L)
                    .build()
                )
                .setMaximum(
                  SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.builder()
                    .setUnit(
                      SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.Unit.BUSINESS_DAY
                    )
                    .setValue(7L)
                    .build()
                )
                .build()
            )
            .build()
        )
        .build()
    )
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setCurrency("usd")
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setUnitAmount(2000L)
            .setTaxBehavior(SessionCreateParams.LineItem.PriceData.TaxBehavior.EXCLUSIVE)
            .build()
        )
        .setQuantity(1L)
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
  billing_address_collection: 'required',
  shipping_address_collection: {
    allowed_countries: ['US', 'CA'],
  },
  shipping_options: [
    {
      shipping_rate_data: {
        type: 'fixed_amount',
        fixed_amount: {
          amount: 0,
          currency: 'usd',
        },
        display_name: 'Free shipping',
        tax_behavior: 'exclusive',
        tax_code: 'txcd_92010001',
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
      },
    },
  ],
  line_items: [
    {
      price_data: {
        currency: 'usd',
        product_data: {
          name: 'T-shirt',
        },
        unit_amount: 2000,
        tax_behavior: 'exclusive',
      },
      quantity: 1,
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
  BillingAddressCollection: stripe.String(stripe.CheckoutSessionBillingAddressCollectionRequired),
  ShippingAddressCollection: &stripe.CheckoutSessionCreateShippingAddressCollectionParams{
    AllowedCountries: []*string{stripe.String("US"), stripe.String("CA")},
  },
  ShippingOptions: []*stripe.CheckoutSessionCreateShippingOptionParams{
    &stripe.CheckoutSessionCreateShippingOptionParams{
      ShippingRateData: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataParams{
        Type: stripe.String("fixed_amount"),
        FixedAmount: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataFixedAmountParams{
          Amount: stripe.Int64(0),
          Currency: stripe.String(stripe.CurrencyUSD),
        },
        DisplayName: stripe.String("Free shipping"),
        TaxBehavior: stripe.String("exclusive"),
        TaxCode: stripe.String("txcd_92010001"),
        DeliveryEstimate: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataDeliveryEstimateParams{
          Minimum: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataDeliveryEstimateMinimumParams{
            Unit: stripe.String("business_day"),
            Value: stripe.Int64(5),
          },
          Maximum: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataDeliveryEstimateMaximumParams{
            Unit: stripe.String("business_day"),
            Value: stripe.Int64(7),
          },
        },
      },
    },
  },
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        Currency: stripe.String(stripe.CurrencyUSD),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        UnitAmount: stripe.Int64(2000),
        TaxBehavior: stripe.String("exclusive"),
      },
      Quantity: stripe.Int64(1),
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
    BillingAddressCollection = "required",
    ShippingAddressCollection = new Stripe.Checkout.SessionShippingAddressCollectionOptions
    {
        AllowedCountries = new List<string> { "US", "CA" },
    },
    ShippingOptions = new List<Stripe.Checkout.SessionShippingOptionOptions>
    {
        new Stripe.Checkout.SessionShippingOptionOptions
        {
            ShippingRateData = new Stripe.Checkout.SessionShippingOptionShippingRateDataOptions
            {
                Type = "fixed_amount",
                FixedAmount = new Stripe.Checkout.SessionShippingOptionShippingRateDataFixedAmountOptions
                {
                    Amount = 0,
                    Currency = "usd",
                },
                DisplayName = "Free shipping",
                TaxBehavior = "exclusive",
                TaxCode = "txcd_92010001",
                DeliveryEstimate = new Stripe.Checkout.SessionShippingOptionShippingRateDataDeliveryEstimateOptions
                {
                    Minimum = new Stripe.Checkout.SessionShippingOptionShippingRateDataDeliveryEstimateMinimumOptions
                    {
                        Unit = "business_day",
                        Value = 5,
                    },
                    Maximum = new Stripe.Checkout.SessionShippingOptionShippingRateDataDeliveryEstimateMaximumOptions
                    {
                        Unit = "business_day",
                        Value = 7,
                    },
                },
            },
        },
    },
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                Currency = "usd",
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                UnitAmount = 2000,
                TaxBehavior = "exclusive",
            },
            Quantity = 1,
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

#### Embedded form

```curl
curl https://api.stripe.com/v1/checkout/sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d billing_address_collection=required \
  -d "shipping_address_collection[allowed_countries][0]"=US \
  -d "shipping_address_collection[allowed_countries][1]"=CA \
  -d "shipping_options[0][shipping_rate_data][type]"=fixed_amount \
  -d "shipping_options[0][shipping_rate_data][fixed_amount][amount]"=0 \
  -d "shipping_options[0][shipping_rate_data][fixed_amount][currency]"=usd \
  -d "shipping_options[0][shipping_rate_data][display_name]"="Free shipping" \
  -d "shipping_options[0][shipping_rate_data][tax_behavior]"=exclusive \
  -d "shipping_options[0][shipping_rate_data][tax_code]"=txcd_92010001 \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][minimum][unit]"=business_day \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][minimum][value]"=5 \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][maximum][unit]"=business_day \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][maximum][value]"=7 \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][unit_amount]"=2000 \
  -d "line_items[0][price_data][tax_behavior]"=exclusive \
  -d "line_items[0][quantity]"=1 \
  -d "automatic_tax[enabled]"=true \
  -d mode=payment \
  -d ui_mode=embedded \
  --data-urlencode return_url="https://example.com/return"
```

```cli
stripe checkout sessions create  \
  --billing-address-collection=required \
  -d "shipping_address_collection[allowed_countries][0]"=US \
  -d "shipping_address_collection[allowed_countries][1]"=CA \
  -d "shipping_options[0][shipping_rate_data][type]"=fixed_amount \
  -d "shipping_options[0][shipping_rate_data][fixed_amount][amount]"=0 \
  -d "shipping_options[0][shipping_rate_data][fixed_amount][currency]"=usd \
  -d "shipping_options[0][shipping_rate_data][display_name]"="Free shipping" \
  -d "shipping_options[0][shipping_rate_data][tax_behavior]"=exclusive \
  -d "shipping_options[0][shipping_rate_data][tax_code]"=txcd_92010001 \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][minimum][unit]"=business_day \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][minimum][value]"=5 \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][maximum][unit]"=business_day \
  -d "shipping_options[0][shipping_rate_data][delivery_estimate][maximum][value]"=7 \
  -d "line_items[0][price_data][currency]"=usd \
  -d "line_items[0][price_data][product_data][name]"=T-shirt \
  -d "line_items[0][price_data][unit_amount]"=2000 \
  -d "line_items[0][price_data][tax_behavior]"=exclusive \
  -d "line_items[0][quantity]"=1 \
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
  billing_address_collection: 'required',
  shipping_address_collection: {allowed_countries: ['US', 'CA']},
  shipping_options: [
    {
      shipping_rate_data: {
        type: 'fixed_amount',
        fixed_amount: {
          amount: 0,
          currency: 'usd',
        },
        display_name: 'Free shipping',
        tax_behavior: 'exclusive',
        tax_code: 'txcd_92010001',
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
      },
    },
  ],
  line_items: [
    {
      price_data: {
        currency: 'usd',
        product_data: {name: 'T-shirt'},
        unit_amount: 2000,
        tax_behavior: 'exclusive',
      },
      quantity: 1,
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
  "billing_address_collection": "required",
  "shipping_address_collection": {"allowed_countries": ["US", "CA"]},
  "shipping_options": [
    {
      "shipping_rate_data": {
        "type": "fixed_amount",
        "fixed_amount": {"amount": 0, "currency": "usd"},
        "display_name": "Free shipping",
        "tax_behavior": "exclusive",
        "tax_code": "txcd_92010001",
        "delivery_estimate": {
          "minimum": {"unit": "business_day", "value": 5},
          "maximum": {"unit": "business_day", "value": 7},
        },
      },
    },
  ],
  "line_items": [
    {
      "price_data": {
        "currency": "usd",
        "product_data": {"name": "T-shirt"},
        "unit_amount": 2000,
        "tax_behavior": "exclusive",
      },
      "quantity": 1,
    },
  ],
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
  'billing_address_collection' => 'required',
  'shipping_address_collection' => ['allowed_countries' => ['US', 'CA']],
  'shipping_options' => [
    [
      'shipping_rate_data' => [
        'type' => 'fixed_amount',
        'fixed_amount' => [
          'amount' => 0,
          'currency' => 'usd',
        ],
        'display_name' => 'Free shipping',
        'tax_behavior' => 'exclusive',
        'tax_code' => 'txcd_92010001',
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
      ],
    ],
  ],
  'line_items' => [
    [
      'price_data' => [
        'currency' => 'usd',
        'product_data' => ['name' => 'T-shirt'],
        'unit_amount' => 2000,
        'tax_behavior' => 'exclusive',
      ],
      'quantity' => 1,
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
    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
    .setShippingAddressCollection(
      SessionCreateParams.ShippingAddressCollection.builder()
        .addAllowedCountry(
          SessionCreateParams.ShippingAddressCollection.AllowedCountry.US
        )
        .addAllowedCountry(
          SessionCreateParams.ShippingAddressCollection.AllowedCountry.CA
        )
        .build()
    )
    .addShippingOption(
      SessionCreateParams.ShippingOption.builder()
        .setShippingRateData(
          SessionCreateParams.ShippingOption.ShippingRateData.builder()
            .setType(
              SessionCreateParams.ShippingOption.ShippingRateData.Type.FIXED_AMOUNT
            )
            .setFixedAmount(
              SessionCreateParams.ShippingOption.ShippingRateData.FixedAmount.builder()
                .setAmount(0L)
                .setCurrency("usd")
                .build()
            )
            .setDisplayName("Free shipping")
            .setTaxBehavior(
              SessionCreateParams.ShippingOption.ShippingRateData.TaxBehavior.EXCLUSIVE
            )
            .setTaxCode("txcd_92010001")
            .setDeliveryEstimate(
              SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.builder()
                .setMinimum(
                  SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.builder()
                    .setUnit(
                      SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Minimum.Unit.BUSINESS_DAY
                    )
                    .setValue(5L)
                    .build()
                )
                .setMaximum(
                  SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.builder()
                    .setUnit(
                      SessionCreateParams.ShippingOption.ShippingRateData.DeliveryEstimate.Maximum.Unit.BUSINESS_DAY
                    )
                    .setValue(7L)
                    .build()
                )
                .build()
            )
            .build()
        )
        .build()
    )
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setCurrency("usd")
            .setProductData(
              SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("T-shirt")
                .build()
            )
            .setUnitAmount(2000L)
            .setTaxBehavior(SessionCreateParams.LineItem.PriceData.TaxBehavior.EXCLUSIVE)
            .build()
        )
        .setQuantity(1L)
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
  billing_address_collection: 'required',
  shipping_address_collection: {
    allowed_countries: ['US', 'CA'],
  },
  shipping_options: [
    {
      shipping_rate_data: {
        type: 'fixed_amount',
        fixed_amount: {
          amount: 0,
          currency: 'usd',
        },
        display_name: 'Free shipping',
        tax_behavior: 'exclusive',
        tax_code: 'txcd_92010001',
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
      },
    },
  ],
  line_items: [
    {
      price_data: {
        currency: 'usd',
        product_data: {
          name: 'T-shirt',
        },
        unit_amount: 2000,
        tax_behavior: 'exclusive',
      },
      quantity: 1,
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
  BillingAddressCollection: stripe.String(stripe.CheckoutSessionBillingAddressCollectionRequired),
  ShippingAddressCollection: &stripe.CheckoutSessionCreateShippingAddressCollectionParams{
    AllowedCountries: []*string{stripe.String("US"), stripe.String("CA")},
  },
  ShippingOptions: []*stripe.CheckoutSessionCreateShippingOptionParams{
    &stripe.CheckoutSessionCreateShippingOptionParams{
      ShippingRateData: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataParams{
        Type: stripe.String("fixed_amount"),
        FixedAmount: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataFixedAmountParams{
          Amount: stripe.Int64(0),
          Currency: stripe.String(stripe.CurrencyUSD),
        },
        DisplayName: stripe.String("Free shipping"),
        TaxBehavior: stripe.String("exclusive"),
        TaxCode: stripe.String("txcd_92010001"),
        DeliveryEstimate: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataDeliveryEstimateParams{
          Minimum: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataDeliveryEstimateMinimumParams{
            Unit: stripe.String("business_day"),
            Value: stripe.Int64(5),
          },
          Maximum: &stripe.CheckoutSessionCreateShippingOptionShippingRateDataDeliveryEstimateMaximumParams{
            Unit: stripe.String("business_day"),
            Value: stripe.Int64(7),
          },
        },
      },
    },
  },
  LineItems: []*stripe.CheckoutSessionCreateLineItemParams{
    &stripe.CheckoutSessionCreateLineItemParams{
      PriceData: &stripe.CheckoutSessionCreateLineItemPriceDataParams{
        Currency: stripe.String(stripe.CurrencyUSD),
        ProductData: &stripe.CheckoutSessionCreateLineItemPriceDataProductDataParams{
          Name: stripe.String("T-shirt"),
        },
        UnitAmount: stripe.Int64(2000),
        TaxBehavior: stripe.String("exclusive"),
      },
      Quantity: stripe.Int64(1),
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
    BillingAddressCollection = "required",
    ShippingAddressCollection = new Stripe.Checkout.SessionShippingAddressCollectionOptions
    {
        AllowedCountries = new List<string> { "US", "CA" },
    },
    ShippingOptions = new List<Stripe.Checkout.SessionShippingOptionOptions>
    {
        new Stripe.Checkout.SessionShippingOptionOptions
        {
            ShippingRateData = new Stripe.Checkout.SessionShippingOptionShippingRateDataOptions
            {
                Type = "fixed_amount",
                FixedAmount = new Stripe.Checkout.SessionShippingOptionShippingRateDataFixedAmountOptions
                {
                    Amount = 0,
                    Currency = "usd",
                },
                DisplayName = "Free shipping",
                TaxBehavior = "exclusive",
                TaxCode = "txcd_92010001",
                DeliveryEstimate = new Stripe.Checkout.SessionShippingOptionShippingRateDataDeliveryEstimateOptions
                {
                    Minimum = new Stripe.Checkout.SessionShippingOptionShippingRateDataDeliveryEstimateMinimumOptions
                    {
                        Unit = "business_day",
                        Value = 5,
                    },
                    Maximum = new Stripe.Checkout.SessionShippingOptionShippingRateDataDeliveryEstimateMaximumOptions
                    {
                        Unit = "business_day",
                        Value = 7,
                    },
                },
            },
        },
    },
    LineItems = new List<Stripe.Checkout.SessionLineItemOptions>
    {
        new Stripe.Checkout.SessionLineItemOptions
        {
            PriceData = new Stripe.Checkout.SessionLineItemPriceDataOptions
            {
                Currency = "usd",
                ProductData = new Stripe.Checkout.SessionLineItemPriceDataProductDataOptions
                {
                    Name = "T-shirt",
                },
                UnitAmount = 2000,
                TaxBehavior = "exclusive",
            },
            Quantity = 1,
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

Your customer can see the calculated tax amount for the shipping rate factored into the total sales tax in your checkout flow:
![Calculated tax amount for the shipping rate on the checkout page](https://b.stripecdn.com/docs-statics-srv/assets/taxed-shipping.14e1bb580c37e035fcf2f0016680db5a.jpg)

Calculated tax amount for the shipping rate in the checkout flow
