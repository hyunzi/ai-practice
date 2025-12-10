# Set product or subscription quantities

Subscribe a customer to multiple products, or to multiple quantities of a single product, all billed in a single invoice.

You might need to create subscriptions for [multiple products](https://docs.stripe.com/billing/subscriptions/quantities.md#multiple-product-sub) or for [multiple quantities of the same product](https://docs.stripe.com/billing/subscriptions/quantities.md#multiple-quantities-sub). You can also create [multiple subscriptions for individual customers](https://docs.stripe.com/billing/subscriptions/quantities.md#multiple-subscriptions).

## Subscriptions with multiple products 

If you offer multiple products or want to charge different amounts for the same product, you can create a subscription for multiple products. This generates a single *invoice* (Invoices are statements of amounts owed by a customer. They track the status of payments from draft through paid or otherwise finalized. Subscriptions automatically generate invoices, or you can manually create a one-off invoice) each billing period that combines every price, and the customer only needs to make a single payment.

### Create a multiple-product subscription

Create multiple-product subscriptions for a customer using the [items](https://docs.stripe.com/api/subscriptions/create.md#create_subscription-items) parameter. Provide the `price` and, optionally, a `quantity` (when using a value other than 1), for each product:

```curl
curl https://api.stripe.com/v1/subscriptions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "items[0][price]"=price_CBXbz9i7AIOTzr \
  -d "items[1][price]"=price_IFuCu48Snc02bc \
  -d "items[1][quantity]"=2
```

```cli
stripe subscriptions create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "items[0][price]"=price_CBXbz9i7AIOTzr \
  -d "items[1][price]"=price_IFuCu48Snc02bc \
  -d "items[1][quantity]"=2
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

subscription = client.v1.subscriptions.create({
  customer: '{{CUSTOMER_ID}}',
  items: [
    {price: 'price_CBXbz9i7AIOTzr'},
    {
      price: 'price_IFuCu48Snc02bc',
      quantity: 2,
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
subscription = client.v1.subscriptions.create({
  "customer": "{{CUSTOMER_ID}}",
  "items": [
    {"price": "price_CBXbz9i7AIOTzr"},
    {"price": "price_IFuCu48Snc02bc", "quantity": 2},
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$subscription = $stripe->subscriptions->create([
  'customer' => '{{CUSTOMER_ID}}',
  'items' => [
    ['price' => 'price_CBXbz9i7AIOTzr'],
    [
      'price' => 'price_IFuCu48Snc02bc',
      'quantity' => 2,
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SubscriptionCreateParams params =
  SubscriptionCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .addItem(
      SubscriptionCreateParams.Item.builder().setPrice("price_CBXbz9i7AIOTzr").build()
    )
    .addItem(
      SubscriptionCreateParams.Item.builder()
        .setPrice("price_IFuCu48Snc02bc")
        .setQuantity(2L)
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Subscription subscription = client.v1().subscriptions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const subscription = await stripe.subscriptions.create({
  customer: '{{CUSTOMER_ID}}',
  items: [
    {
      price: 'price_CBXbz9i7AIOTzr',
    },
    {
      price: 'price_IFuCu48Snc02bc',
      quantity: 2,
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SubscriptionCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  Items: []*stripe.SubscriptionCreateItemParams{
    &stripe.SubscriptionCreateItemParams{Price: stripe.String("price_CBXbz9i7AIOTzr")},
    &stripe.SubscriptionCreateItemParams{
      Price: stripe.String("price_IFuCu48Snc02bc"),
      Quantity: stripe.Int64(2),
    },
  },
}
result, err := sc.V1Subscriptions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new SubscriptionCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    Items = new List<SubscriptionItemOptions>
    {
        new SubscriptionItemOptions { Price = "price_CBXbz9i7AIOTzr" },
        new SubscriptionItemOptions { Price = "price_IFuCu48Snc02bc", Quantity = 2 },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Subscriptions;
Subscription subscription = service.Create(options);
```

The response includes a list of all the subscription items, prices, and quantities:

```json
{
  "id": "sub_CZEpS1Zt9QLxdo",
  "object": "subscription",
  ...
  "items": {
    "object": "list",
    "data": [
      {
        "id": "si_H1yPnAVzP9vDRW",
        "object": "subscription_item",
        "billing_thresholds": null,
        "created": 1585939321,
        "metadata": {
        },
        "price": {
          "id": "price_H1c8v1liEvrfcd",
          "object": "price",
          "active": true,
          "billing_scheme": "per_unit",
          "created": 1585856460,
          "currency": "usd",
          "livemode": false,
          "lookup_key": null,
          "metadata": {
          },
          "nickname": null,
          "product": "prod_H1c7exjJHbC4sr",
          "recurring": {
            "interval": "month",
            "interval_count": 1,
            "trial_period_days": null,
            "usage_type": "licensed"
          },
          "tiers": null,
          "tiers_mode": null,
          "transform_quantity": null,
          "type": "recurring",
          "unit_amount": 1000,
          "unit_amount_decimal": "1000"
        },
        "quantity": 1,
        "subscription": "sub_H1yPRslJXa4TUt",
        "tax_rates": [
        ]
      },
      {
        "id": "si_H1yPu4fSjq3oqM",
        "object": "subscription_item",
        "billing_thresholds": null,
        "created": 1585939321,
        "metadata": {
        },
        "price": {
          "id": "price_H1yCssogQ6gtx1",
          "object": "price",
          "active": true,
          "billing_scheme": "per_unit",
          "created": 1585938535,
          "currency": "usd",
          "livemode": false,
          "lookup_key": null,
          "metadata": {
          },
          "nickname": null,
          "product": "prod_H1c7exjJHbC4sr",
          "recurring": {
            "interval": "month",
            "interval_count": 1,
            "trial_period_days": null,
            "usage_type": "licensed"
          },
          "tiers": null,
          "tiers_mode": null,
          "transform_quantity": null,
          "type": "recurring",
          "unit_amount": 2000,
          "unit_amount_decimal": "2000"
        },
        "quantity": 2,
        "subscription": "sub_H1yPRslJXa4TUt",
        "tax_rates": [
        ]
      }
    ]
  },
  ...
}
```

### Billing periods with multiple prices

Subscriptions that charge a fixed rate on an interval are billed at the start of each [billing period (cycle)](https://docs.stripe.com/billing/subscriptions/billing-cycle.md). With each invoice, the customer effectively pays for the next interval of service. With [usage-based billing](https://docs.stripe.com/products-prices/pricing-models.md#usage-based-pricing), the amount paid by the customer varies based on consumption during the billing period, so the customer pays for their usage at the end.

When a subscription combines a fixed rate with usage-based billing, metered usage from the previous billing period is charged alongside the fixed rate for the new billing period at the start of each renewal. The usage-based billing and fixed rate are combined in a single invoice.

Since using multiple products with a subscription results in a single invoice and payment, all the prices for those products must use the same currency. You?�re also limited to 20 products in a single subscription.

To handle multiple products in a subscription that might have different billing periods, you can create a [mixed interval subscription](https://docs.stripe.com/billing/subscriptions/mixed-interval.md).

### Discounts, taxes, and trial periods 

When using multiple products, you can also create [discounts](https://docs.stripe.com/billing/subscriptions/coupons.md), charge [taxes](https://docs.stripe.com/billing/taxes/collect-taxes.md), and use [trial periods](https://docs.stripe.com/billing/subscriptions/trials.md) the same way you would with a single-product subscription.

Provide these arguments when you [create](https://docs.stripe.com/api/subscriptions/create.md) or [update](https://docs.stripe.com/api/subscriptions/update.md) a subscription to apply them to the whole subscription, or provide them at the item level to only discount a specific item.

```curl
curl https://api.stripe.com/v1/subscriptions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "discounts[0][coupon]"=free-period \
  -d "default_tax_rates[0]"=txr_1EO66sClCIKljWvs98IiVfHW \
  -d trial_end=1610403705 \
  -d "items[0][price]"=price_CBXbz9i7AIOTzr \
  -d "items[1][price]"=price_IFuCu48Snc02bc \
  -d "items[1][quantity]"=2
```

## Multiple quantities of a subscription 

Setting a quantity on a subscription is often described as ?�per-seat licensing?? which has a linear cost increase: 10 uses incurs a cost of 10 times the base price.

By default, each subscription is for one product, but Stripe allows you to subscribe a customer to multiple quantities of an item. For example, say you run a hosting company and your customers host sites through it at a cost of 9.99 USD per site, per month. Most customers host a single site, while some host many. You could create prices for one site (9.99 USD), two sites (19.98 USD), and so forth, but a better approach is to subscribe customers to a quantity with a 9.99 USD unit price.

Subscriptions have two kinds of usage-based billing: metered and per-seat licensing. You can enable these billing models by setting the value of the `recurring[usage_type]` attribute when creating a price. You can only specify a quantity when creating a subscription with a `recurring[usage_type]` of `licensed`. If you want to have granular billing for usage that fluctuates within a billing interval, consider using [usage-based billing](https://docs.stripe.com/products-prices/pricing-models.md#usage-based-pricing) instead of quantities.

### Set multiple quantities 

To set the quantity on a subscription, provide a `quantity` value when [creating](https://docs.stripe.com/api.md#create_subscription) or [updating](https://docs.stripe.com/api.md#update_subscription) the subscription:

```curl
curl https://api.stripe.com/v1/subscriptions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer=cus_4fdAW5ftNQow1a \
  -d "items[0][price]"=price_CBb6IXqvTLXp3f \
  -d "items[0][quantity]"=5
```

```cli
stripe subscriptions create  \
  --customer=cus_4fdAW5ftNQow1a \
  -d "items[0][price]"=price_CBb6IXqvTLXp3f \
  -d "items[0][quantity]"=5
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

subscription = client.v1.subscriptions.create({
  customer: 'cus_4fdAW5ftNQow1a',
  items: [
    {
      price: 'price_CBb6IXqvTLXp3f',
      quantity: 5,
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
subscription = client.v1.subscriptions.create({
  "customer": "cus_4fdAW5ftNQow1a",
  "items": [{"price": "price_CBb6IXqvTLXp3f", "quantity": 5}],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$subscription = $stripe->subscriptions->create([
  'customer' => 'cus_4fdAW5ftNQow1a',
  'items' => [
    [
      'price' => 'price_CBb6IXqvTLXp3f',
      'quantity' => 5,
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SubscriptionCreateParams params =
  SubscriptionCreateParams.builder()
    .setCustomer("cus_4fdAW5ftNQow1a")
    .addItem(
      SubscriptionCreateParams.Item.builder()
        .setPrice("price_CBb6IXqvTLXp3f")
        .setQuantity(5L)
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Subscription subscription = client.v1().subscriptions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const subscription = await stripe.subscriptions.create({
  customer: 'cus_4fdAW5ftNQow1a',
  items: [
    {
      price: 'price_CBb6IXqvTLXp3f',
      quantity: 5,
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SubscriptionCreateParams{
  Customer: stripe.String("cus_4fdAW5ftNQow1a"),
  Items: []*stripe.SubscriptionCreateItemParams{
    &stripe.SubscriptionCreateItemParams{
      Price: stripe.String("price_CBb6IXqvTLXp3f"),
      Quantity: stripe.Int64(5),
    },
  },
}
result, err := sc.V1Subscriptions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new SubscriptionCreateOptions
{
    Customer = "cus_4fdAW5ftNQow1a",
    Items = new List<SubscriptionItemOptions>
    {
        new SubscriptionItemOptions { Price = "price_CBb6IXqvTLXp3f", Quantity = 5 },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Subscriptions;
Subscription subscription = service.Create(options);
```

You still bill multiple quantities using one *invoice* (Invoices are statements of amounts owed by a customer. They track the status of payments from draft through paid or otherwise finalized. Subscriptions automatically generate invoices, or you can manually create a one-off invoice), and you [prorate](https://docs.stripe.com/billing/subscriptions/prorations.md) them when the subscription changes. This includes when you change subscription quantities.

### Charge different amounts based on quantity

In some cases, you might want to adjust the cost per seat based on the number of seats in a subscription. For example, you could offer volume licensing discounts for subscriptions that exceed certain quantity thresholds. You can use [tiers](https://docs.stripe.com/products-prices/pricing-models.md#tiered-pricing) to adjust per-seat pricing.

### Quantity transformation

When you bill your customers, you might want to track usage at a different granularity than you bill. For example, consider a productivity software suite that charges 10 USD for every 5 users (or portion thereof) using the product. Without quantity transformation, they would need to increase the `quantity` of the subscription item by 1 for every 5 users.

| Number of Users | Subscription Item Quantity Reported to Stripe | Total  |
| --------------- | --------------------------------------------- | ------ |
| 1               | 1                                             | 10 USD |
| 3               | 1                                             | 10 USD |
| 5               | 1                                             | 10 USD |
| 6               | 2                                             | 20 USD |
| 7               | 2                                             | 20 USD |

With the [transform_quantity](https://docs.stripe.com/api/prices/create.md#create_price-transform_quantity) parameter, you can instruct Stripe to transform the quantity before applying the per unit cost. The following subscription allows you to naturally report the current number of users as the subscription item `quantity`. Stripe?�s billing system divides the quantity by 5 and rounds up before calculating by the unit cost.

```curl
curl https://api.stripe.com/v1/prices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d nickname="Standard Cost Per 5 Users" \
  -d "transform_quantity[divide_by]"=5 \
  -d "transform_quantity[round]"=up \
  -d unit_amount=1000 \
  -d currency=usd \
  -d "recurring[interval]"=month \
  -d "recurring[usage_type]"=licensed \
  -d product={{PRODUCTIVITY_SUITE_ID}}
```

```cli
stripe prices create  \
  --nickname="Standard Cost Per 5 Users" \
  -d "transform_quantity[divide_by]"=5 \
  -d "transform_quantity[round]"=up \
  --unit-amount=1000 \
  --currency=usd \
  -d "recurring[interval]"=month \
  -d "recurring[usage_type]"=licensed \
  --product={{PRODUCTIVITY_SUITE_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

price = client.v1.prices.create({
  nickname: 'Standard Cost Per 5 Users',
  transform_quantity: {
    divide_by: 5,
    round: 'up',
  },
  unit_amount: 1000,
  currency: 'usd',
  recurring: {
    interval: 'month',
    usage_type: 'licensed',
  },
  product: '{{PRODUCTIVITY_SUITE_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
price = client.v1.prices.create({
  "nickname": "Standard Cost Per 5 Users",
  "transform_quantity": {"divide_by": 5, "round": "up"},
  "unit_amount": 1000,
  "currency": "usd",
  "recurring": {"interval": "month", "usage_type": "licensed"},
  "product": "{{PRODUCTIVITY_SUITE_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$price = $stripe->prices->create([
  'nickname' => 'Standard Cost Per 5 Users',
  'transform_quantity' => [
    'divide_by' => 5,
    'round' => 'up',
  ],
  'unit_amount' => 1000,
  'currency' => 'usd',
  'recurring' => [
    'interval' => 'month',
    'usage_type' => 'licensed',
  ],
  'product' => '{{PRODUCTIVITY_SUITE_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PriceCreateParams params =
  PriceCreateParams.builder()
    .setNickname("Standard Cost Per 5 Users")
    .setTransformQuantity(
      PriceCreateParams.TransformQuantity.builder()
        .setDivideBy(5L)
        .setRound(PriceCreateParams.TransformQuantity.Round.UP)
        .build()
    )
    .setUnitAmount(1000L)
    .setCurrency("usd")
    .setRecurring(
      PriceCreateParams.Recurring.builder()
        .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
        .setUsageType(PriceCreateParams.Recurring.UsageType.LICENSED)
        .build()
    )
    .setProduct("{{PRODUCTIVITY_SUITE_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Price price = client.v1().prices().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const price = await stripe.prices.create({
  nickname: 'Standard Cost Per 5 Users',
  transform_quantity: {
    divide_by: 5,
    round: 'up',
  },
  unit_amount: 1000,
  currency: 'usd',
  recurring: {
    interval: 'month',
    usage_type: 'licensed',
  },
  product: '{{PRODUCTIVITY_SUITE_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PriceCreateParams{
  Nickname: stripe.String("Standard Cost Per 5 Users"),
  TransformQuantity: &stripe.PriceCreateTransformQuantityParams{
    DivideBy: stripe.Int64(5),
    Round: stripe.String(stripe.PriceTransformQuantityRoundUp),
  },
  UnitAmount: stripe.Int64(1000),
  Currency: stripe.String(stripe.CurrencyUSD),
  Recurring: &stripe.PriceCreateRecurringParams{
    Interval: stripe.String(stripe.PriceRecurringIntervalMonth),
    UsageType: stripe.String(stripe.PriceRecurringUsageTypeLicensed),
  },
  Product: stripe.String("{{PRODUCTIVITY_SUITE_ID}}"),
}
result, err := sc.V1Prices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PriceCreateOptions
{
    Nickname = "Standard Cost Per 5 Users",
    TransformQuantity = new PriceTransformQuantityOptions { DivideBy = 5, Round = "up" },
    UnitAmount = 1000,
    Currency = "usd",
    Recurring = new PriceRecurringOptions { Interval = "month", UsageType = "licensed" },
    Product = "{{PRODUCTIVITY_SUITE_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Prices;
Price price = service.Create(options);
```

You can only use `transform_quantity` with [billing_scheme=per_unit](https://docs.stripe.com/api/prices/create.md#create_price-billing_scheme). It?�s incompatible with tiered pricing.

#### Rounding

The previous example showed a subscription that charges for every 5 users rounding up, that is, 6 divided by 5 results in a quantity of 2. For use cases where you don?�t want to charge for a portion of usage, like charging for every full gigabyte of usage of a broadband internet service, you can also pass `down` as the value of `round`.

#### Metered usage

You can also apply `transform_quantity` in conjunction with usage-based billing. This transformation applies to prices with `recurring[usage_type]=metered` at the end of a billing period in the same way it applies to `quantity` for prices with `recurring[usage_type]=licensed`.

A marketing email service that creates a metered price to charge 0.10 USD for every 1000 emails sent might look like:

```curl
curl https://api.stripe.com/v1/prices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d nickname="Metered Emails" \
  -d "transform_quantity[divide_by]"=1000 \
  -d "transform_quantity[round]"=down \
  -d unit_amount=10 \
  -d currency=usd \
  -d "recurring[interval]"=month \
  -d "recurring[usage_type]"=metered \
  -d product={{MARKETING_EMAILS_ID}}
```

```cli
stripe prices create  \
  --nickname="Metered Emails" \
  -d "transform_quantity[divide_by]"=1000 \
  -d "transform_quantity[round]"=down \
  --unit-amount=10 \
  --currency=usd \
  -d "recurring[interval]"=month \
  -d "recurring[usage_type]"=metered \
  --product={{MARKETING_EMAILS_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

price = client.v1.prices.create({
  nickname: 'Metered Emails',
  transform_quantity: {
    divide_by: 1000,
    round: 'down',
  },
  unit_amount: 10,
  currency: 'usd',
  recurring: {
    interval: 'month',
    usage_type: 'metered',
  },
  product: '{{MARKETING_EMAILS_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
price = client.v1.prices.create({
  "nickname": "Metered Emails",
  "transform_quantity": {"divide_by": 1000, "round": "down"},
  "unit_amount": 10,
  "currency": "usd",
  "recurring": {"interval": "month", "usage_type": "metered"},
  "product": "{{MARKETING_EMAILS_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$price = $stripe->prices->create([
  'nickname' => 'Metered Emails',
  'transform_quantity' => [
    'divide_by' => 1000,
    'round' => 'down',
  ],
  'unit_amount' => 10,
  'currency' => 'usd',
  'recurring' => [
    'interval' => 'month',
    'usage_type' => 'metered',
  ],
  'product' => '{{MARKETING_EMAILS_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PriceCreateParams params =
  PriceCreateParams.builder()
    .setNickname("Metered Emails")
    .setTransformQuantity(
      PriceCreateParams.TransformQuantity.builder()
        .setDivideBy(1000L)
        .setRound(PriceCreateParams.TransformQuantity.Round.DOWN)
        .build()
    )
    .setUnitAmount(10L)
    .setCurrency("usd")
    .setRecurring(
      PriceCreateParams.Recurring.builder()
        .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
        .setUsageType(PriceCreateParams.Recurring.UsageType.METERED)
        .build()
    )
    .setProduct("{{MARKETING_EMAILS_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Price price = client.v1().prices().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const price = await stripe.prices.create({
  nickname: 'Metered Emails',
  transform_quantity: {
    divide_by: 1000,
    round: 'down',
  },
  unit_amount: 10,
  currency: 'usd',
  recurring: {
    interval: 'month',
    usage_type: 'metered',
  },
  product: '{{MARKETING_EMAILS_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PriceCreateParams{
  Nickname: stripe.String("Metered Emails"),
  TransformQuantity: &stripe.PriceCreateTransformQuantityParams{
    DivideBy: stripe.Int64(1000),
    Round: stripe.String(stripe.PriceTransformQuantityRoundDown),
  },
  UnitAmount: stripe.Int64(10),
  Currency: stripe.String(stripe.CurrencyUSD),
  Recurring: &stripe.PriceCreateRecurringParams{
    Interval: stripe.String(stripe.PriceRecurringIntervalMonth),
    UsageType: stripe.String(stripe.PriceRecurringUsageTypeMetered),
  },
  Product: stripe.String("{{MARKETING_EMAILS_ID}}"),
}
result, err := sc.V1Prices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PriceCreateOptions
{
    Nickname = "Metered Emails",
    TransformQuantity = new PriceTransformQuantityOptions
    {
        DivideBy = 1000,
        Round = "down",
    },
    UnitAmount = 10,
    Currency = "usd",
    Recurring = new PriceRecurringOptions { Interval = "month", UsageType = "metered" },
    Product = "{{MARKETING_EMAILS_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Prices;
Price price = service.Create(options);
```

With this subscription, usage can be reported per email and you can bill the customer 0.10 USD for every 1000 emails they send.

## Multiple subscriptions

You can simultaneously create multiple subscriptions for a single customer. This capability is useful when you want to make it possible for your customers to subscribe to multiple products with separate service periods. Each subscription has its own separate billing period, invoice, and charge, even if the underlying prices have the same billing period.

> If you create multiple subscriptions with the same price, each subscription is independent, including payments and billing periods. If that?�s not what you want, create a single subscription using [multiple quantities](https://docs.stripe.com/billing/subscriptions/quantities.md#setting-quantities) instead.

### Create multiple subscriptions for one customer

Create multiple subscriptions for one customer by using the [create subscription](https://docs.stripe.com/api.md#create_subscription) API:

```curl
curl https://api.stripe.com/v1/subscriptions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer=cus_4fdAW5ftNQow1a \
  -d "items[0][price]"=price_CZB2krKbBDOkTS
```

```cli
stripe subscriptions create  \
  --customer=cus_4fdAW5ftNQow1a \
  -d "items[0][price]"=price_CZB2krKbBDOkTS
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

subscription = client.v1.subscriptions.create({
  customer: 'cus_4fdAW5ftNQow1a',
  items: [{price: 'price_CZB2krKbBDOkTS'}],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
subscription = client.v1.subscriptions.create({
  "customer": "cus_4fdAW5ftNQow1a",
  "items": [{"price": "price_CZB2krKbBDOkTS"}],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$subscription = $stripe->subscriptions->create([
  'customer' => 'cus_4fdAW5ftNQow1a',
  'items' => [['price' => 'price_CZB2krKbBDOkTS']],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SubscriptionCreateParams params =
  SubscriptionCreateParams.builder()
    .setCustomer("cus_4fdAW5ftNQow1a")
    .addItem(
      SubscriptionCreateParams.Item.builder().setPrice("price_CZB2krKbBDOkTS").build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Subscription subscription = client.v1().subscriptions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const subscription = await stripe.subscriptions.create({
  customer: 'cus_4fdAW5ftNQow1a',
  items: [
    {
      price: 'price_CZB2krKbBDOkTS',
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SubscriptionCreateParams{
  Customer: stripe.String("cus_4fdAW5ftNQow1a"),
  Items: []*stripe.SubscriptionCreateItemParams{
    &stripe.SubscriptionCreateItemParams{Price: stripe.String("price_CZB2krKbBDOkTS")},
  },
}
result, err := sc.V1Subscriptions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new SubscriptionCreateOptions
{
    Customer = "cus_4fdAW5ftNQow1a",
    Items = new List<SubscriptionItemOptions>
    {
        new SubscriptionItemOptions { Price = "price_CZB2krKbBDOkTS" },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Subscriptions;
Subscription subscription = service.Create(options);
```

```curl
curl https://api.stripe.com/v1/subscriptions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer=cus_4fdAW5ftNQow1a \
  -d "items[0][price]"=price_CZB1AX3KOacNJw
```

```cli
stripe subscriptions create  \
  --customer=cus_4fdAW5ftNQow1a \
  -d "items[0][price]"=price_CZB1AX3KOacNJw
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

subscription = client.v1.subscriptions.create({
  customer: 'cus_4fdAW5ftNQow1a',
  items: [{price: 'price_CZB1AX3KOacNJw'}],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
subscription = client.v1.subscriptions.create({
  "customer": "cus_4fdAW5ftNQow1a",
  "items": [{"price": "price_CZB1AX3KOacNJw"}],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$subscription = $stripe->subscriptions->create([
  'customer' => 'cus_4fdAW5ftNQow1a',
  'items' => [['price' => 'price_CZB1AX3KOacNJw']],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SubscriptionCreateParams params =
  SubscriptionCreateParams.builder()
    .setCustomer("cus_4fdAW5ftNQow1a")
    .addItem(
      SubscriptionCreateParams.Item.builder().setPrice("price_CZB1AX3KOacNJw").build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Subscription subscription = client.v1().subscriptions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const subscription = await stripe.subscriptions.create({
  customer: 'cus_4fdAW5ftNQow1a',
  items: [
    {
      price: 'price_CZB1AX3KOacNJw',
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SubscriptionCreateParams{
  Customer: stripe.String("cus_4fdAW5ftNQow1a"),
  Items: []*stripe.SubscriptionCreateItemParams{
    &stripe.SubscriptionCreateItemParams{Price: stripe.String("price_CZB1AX3KOacNJw")},
  },
}
result, err := sc.V1Subscriptions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new SubscriptionCreateOptions
{
    Customer = "cus_4fdAW5ftNQow1a",
    Items = new List<SubscriptionItemOptions>
    {
        new SubscriptionItemOptions { Price = "price_CZB1AX3KOacNJw" },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Subscriptions;
Subscription subscription = service.Create(options);
```

A customer can subscribe to multiple products, or even to a single product, multiple times. Each subscription has a unique ID and its state is handled independently from the customer?�s other subscriptions. Each subscription also has its own independent billing period, based on the [billing cycle anchor](https://docs.stripe.com/billing/subscriptions/billing-cycle.md) of the subscription.

When a customer has multiple subscriptions, the `Customer` object?�s `subscriptions` property provides a list of every subscription:

```json
{
  "id": "cus_4fdAW5ftNQow1a",
  "object": "customer",
  "subscriptions": {
    "object": "list",
    "data": [
      {
        "id": "sub_9RRl3XywPg2P5H",
        "object": "subscription",
        ...
        "price": {
          "id": "price_CZB2krKbBDOkTS",
          "object": "price",
          "amount": 2995,
          ...
        }
      },
      {
        "id": "sub_9RRlIq2t9obFLI",
        "object": "subscription",
        ...
        "price": {
          "id": "price_CZB1AX3KOacNJw",
          "object": "price",
          "amount": 1295,
          ...
        }
      }
    ]
    ...
  }
  ...
}
```

## See also

- [Change subscriptions](https://docs.stripe.com/billing/subscriptions/change.md)
- [Use trial periods](https://docs.stripe.com/billing/subscriptions/trials.md)
- [Subscriptions API](https://docs.stripe.com/api.md#subscriptions)
