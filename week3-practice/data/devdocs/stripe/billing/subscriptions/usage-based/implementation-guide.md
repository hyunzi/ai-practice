# Set up a pay-as-you-go pricing model

Charge customers based on their usage of your product or service.

[Pay-as-you-go pricing](https://docs.stripe.com/products-prices/pricing-models.md#pay-as-you-go) is a flexible, scalable model that lets you charge customers in arrears for the usage they accrue. AI businesses, SaaS platforms, and cloud services often use this pricing model.

## What you?™ll build

This guide describes how to implement pay-as-you-go pricing on Stripe for a fictional company called Hypernian. Hypernian charges their customers the following rates for their LLM models:

| Usage | Fee                     |
| ----- | ----------------------- |
| Token | 0.04 USD per 100 tokens |

To implement this pricing model, you create a meter, set up pricing and billing, and send meter events to record customer usage using [Products](https://docs.stripe.com/api/products.md) and [Prices](https://docs.stripe.com/api/prices.md).

## Create a meter

Meters specify how to aggregate meter events over a billing period. Meter events represent all actions that customers take in your system (for example, API requests). Meters attach to prices and form the basis of what?™s billed.

For the Hypernian example, meter events are the number of tokens a customer uses in a query. The meter is the sum of tokens over a month.

You can use the Stripe Dashboard or API to configure a meter. To use the API with the Stripe CLI to create a meter, [get started with the Stripe CLI](https://docs.stripe.com/stripe-cli.md).

#### Dashboard

1. On the [Meters](https://dashboard.stripe.com/test/meters) page, click **Create meter**.
1. In the meter editor:
   - For **Meter name**, enter the name of the meter to display and for organization purposes. For the Hypernian example, enter ?œHypernian tokens.??   - For **Event name**, enter the name to display in meter events when reporting usage to Stripe. For the Hypernian example, enter ?œhypernian_tokens.??   - Set the **Aggregation method** in the dropdown:
     - For the Hypernian example, select **Sum**. This will *sum the values* reported (in this example, number of tokens a customer uses) to determine the usage to bill for.
     - Choose **Count** to bill based on the *number* of events reported.
     - Choose **Last** to bill based on the *last value* reported.
     - Use the preview pane to set example usage events and verify the aggregation method.
   - Click **Create meter**.
   - (Optional) Under **Advanced settings**, specify the **Dimensions** that you want to tag your usage data with. To generate granular segment specific alerts, or to granularly price usage based on a combination of attributes, submit your usage data with dimensions that are populated for analytics and reporting. Some example dimensions are LLM model, token type, region, and event type.

#### API

```curl
curl https://api.stripe.com/v1/billing/meters \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d display_name="Hypernian tokens" \
  -d event_name=hypernian_tokens \
  -d "default_aggregation[formula]"=sum \
  -d "customer_mapping[event_payload_key]"=stripe_customer_id \
  -d "customer_mapping[type]"=by_id \
  -d "value_settings[event_payload_key]"=value
```

```cli
stripe billing meters create  \
  --display-name="Hypernian tokens" \
  --event-name=hypernian_tokens \
  -d "default_aggregation[formula]"=sum \
  -d "customer_mapping[event_payload_key]"=stripe_customer_id \
  -d "customer_mapping[type]"=by_id \
  -d "value_settings[event_payload_key]"=value
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

meter = client.v1.billing.meters.create({
  display_name: 'Hypernian tokens',
  event_name: 'hypernian_tokens',
  default_aggregation: {formula: 'sum'},
  customer_mapping: {
    event_payload_key: 'stripe_customer_id',
    type: 'by_id',
  },
  value_settings: {event_payload_key: 'value'},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
meter = client.v1.billing.meters.create({
  "display_name": "Hypernian tokens",
  "event_name": "hypernian_tokens",
  "default_aggregation": {"formula": "sum"},
  "customer_mapping": {"event_payload_key": "stripe_customer_id", "type": "by_id"},
  "value_settings": {"event_payload_key": "value"},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$meter = $stripe->billing->meters->create([
  'display_name' => 'Hypernian tokens',
  'event_name' => 'hypernian_tokens',
  'default_aggregation' => ['formula' => 'sum'],
  'customer_mapping' => [
    'event_payload_key' => 'stripe_customer_id',
    'type' => 'by_id',
  ],
  'value_settings' => ['event_payload_key' => 'value'],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

MeterCreateParams params =
  MeterCreateParams.builder()
    .setDisplayName("Hypernian tokens")
    .setEventName("hypernian_tokens")
    .setDefaultAggregation(
      MeterCreateParams.DefaultAggregation.builder()
        .setFormula(MeterCreateParams.DefaultAggregation.Formula.SUM)
        .build()
    )
    .setCustomerMapping(
      MeterCreateParams.CustomerMapping.builder()
        .setEventPayloadKey("stripe_customer_id")
        .setType(MeterCreateParams.CustomerMapping.Type.BY_ID)
        .build()
    )
    .setValueSettings(
      MeterCreateParams.ValueSettings.builder().setEventPayloadKey("value").build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Meter meter = client.v1().billing().meters().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const meter = await stripe.billing.meters.create({
  display_name: 'Hypernian tokens',
  event_name: 'hypernian_tokens',
  default_aggregation: {
    formula: 'sum',
  },
  customer_mapping: {
    event_payload_key: 'stripe_customer_id',
    type: 'by_id',
  },
  value_settings: {
    event_payload_key: 'value',
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.BillingMeterCreateParams{
  DisplayName: stripe.String("Hypernian tokens"),
  EventName: stripe.String("hypernian_tokens"),
  DefaultAggregation: &stripe.BillingMeterCreateDefaultAggregationParams{
    Formula: stripe.String(stripe.BillingMeterDefaultAggregationFormulaSum),
  },
  CustomerMapping: &stripe.BillingMeterCreateCustomerMappingParams{
    EventPayloadKey: stripe.String("stripe_customer_id"),
    Type: stripe.String("by_id"),
  },
  ValueSettings: &stripe.BillingMeterCreateValueSettingsParams{
    EventPayloadKey: stripe.String("value"),
  },
}
result, err := sc.V1BillingMeters.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Billing.MeterCreateOptions
{
    DisplayName = "Hypernian tokens",
    EventName = "hypernian_tokens",
    DefaultAggregation = new Stripe.Billing.MeterDefaultAggregationOptions
    {
        Formula = "sum",
    },
    CustomerMapping = new Stripe.Billing.MeterCustomerMappingOptions
    {
        EventPayloadKey = "stripe_customer_id",
        Type = "by_id",
    },
    ValueSettings = new Stripe.Billing.MeterValueSettingsOptions
    {
        EventPayloadKey = "value",
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Billing.Meters;
Stripe.Billing.Meter meter = service.Create(options);
```

## Create a pricing model

Use the Stripe Dashboard or API to create a [pricing model](https://docs.stripe.com/products-prices/pricing-models.md) that includes your [Products](https://docs.stripe.com/api/products.md) and their pricing options. [Prices](https://docs.stripe.com/api/prices.md) define the unit cost, currency, and billing period.

For the Hypernian example, you create a product with a metered price of 0.04 USD per hundred units, billed at a monthly interval. You use the meter that you created in the previous step.

#### Dashboard

1. On the [Product catalog](https://dashboard.stripe.com/products?active=true) page, click **Create product**.
1. On the **Add a product** page, do the following:
   - For **Name**, enter the name of your product. For the Hypernian example, enter `Hypernian`.
   - (Optional) For **Description**, add a description that appears in [Checkout](https://docs.stripe.com/payments/checkout.md), in the [customer portal](https://docs.stripe.com/customer-management.md) and in [quotes](https://docs.stripe.com/quotes.md).
   - Select **Recurring**.
   - Under **Billing period**, select **More pricing options**.
1. On the **Add price** page, do the following:
   - Under **Choose your pricing model**, select **Usage-based**.
   - Choose your pricing structure:
     - For the Hypernian example, select **Per package**. Under **Price**, set the **Amount** to `0.04 USD` per `100` units.
     - Select **Per unit** to price by number of users, units, or seats.
     - Select **Per tier** to enable [tiered pricing](https://docs.stripe.com/products-prices/pricing-models.md#tiered-pricing) and change the unit cost with quantity or usage.
   - Under **Meter**, select the meter you created in step 1. For the Hypernian example, select **Hypernian tokens** from the dropdown.
   - Select the appropriate **Billing period**. For the Hypernian example, select **Monthly**.
   - Click **Next**.
1. On the **Add a product** page, click **Add product**.

#### API

You can locate your meter ID on the meter details page.

```curl
curl https://api.stripe.com/v1/prices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d currency=usd \
  -d unit_amount=4 \
  -d billing_scheme=per_unit \
  -d "transform_quantity[divide_by]"=100 \
  -d "transform_quantity[round]"=up \
  -d "recurring[usage_type]"=metered \
  -d "recurring[interval]"=month \
  -d "recurring[meter]"={{METER_ID}} \
  -d "product_data[name]"="Hypernian tokens"
```

```cli
stripe prices create  \
  --currency=usd \
  --unit-amount=4 \
  --billing-scheme=per_unit \
  -d "transform_quantity[divide_by]"=100 \
  -d "transform_quantity[round]"=up \
  -d "recurring[usage_type]"=metered \
  -d "recurring[interval]"=month \
  -d "recurring[meter]"={{METER_ID}} \
  -d "product_data[name]"="Hypernian tokens"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

price = client.v1.prices.create({
  currency: 'usd',
  unit_amount: 4,
  billing_scheme: 'per_unit',
  transform_quantity: {
    divide_by: 100,
    round: 'up',
  },
  recurring: {
    usage_type: 'metered',
    interval: 'month',
    meter: '{{METER_ID}}',
  },
  product_data: {name: 'Hypernian tokens'},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
price = client.v1.prices.create({
  "currency": "usd",
  "unit_amount": 4,
  "billing_scheme": "per_unit",
  "transform_quantity": {"divide_by": 100, "round": "up"},
  "recurring": {"usage_type": "metered", "interval": "month", "meter": "{{METER_ID}}"},
  "product_data": {"name": "Hypernian tokens"},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$price = $stripe->prices->create([
  'currency' => 'usd',
  'unit_amount' => 4,
  'billing_scheme' => 'per_unit',
  'transform_quantity' => [
    'divide_by' => 100,
    'round' => 'up',
  ],
  'recurring' => [
    'usage_type' => 'metered',
    'interval' => 'month',
    'meter' => '{{METER_ID}}',
  ],
  'product_data' => ['name' => 'Hypernian tokens'],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PriceCreateParams params =
  PriceCreateParams.builder()
    .setCurrency("usd")
    .setUnitAmount(4L)
    .setBillingScheme(PriceCreateParams.BillingScheme.PER_UNIT)
    .setTransformQuantity(
      PriceCreateParams.TransformQuantity.builder()
        .setDivideBy(100L)
        .setRound(PriceCreateParams.TransformQuantity.Round.UP)
        .build()
    )
    .setRecurring(
      PriceCreateParams.Recurring.builder()
        .setUsageType(PriceCreateParams.Recurring.UsageType.METERED)
        .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
        .setMeter("{{METER_ID}}")
        .build()
    )
    .setProductData(
      PriceCreateParams.ProductData.builder().setName("Hypernian tokens").build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Price price = client.v1().prices().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const price = await stripe.prices.create({
  currency: 'usd',
  unit_amount: 4,
  billing_scheme: 'per_unit',
  transform_quantity: {
    divide_by: 100,
    round: 'up',
  },
  recurring: {
    usage_type: 'metered',
    interval: 'month',
    meter: '{{METER_ID}}',
  },
  product_data: {
    name: 'Hypernian tokens',
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PriceCreateParams{
  Currency: stripe.String(stripe.CurrencyUSD),
  UnitAmount: stripe.Int64(4),
  BillingScheme: stripe.String(stripe.PriceBillingSchemePerUnit),
  TransformQuantity: &stripe.PriceCreateTransformQuantityParams{
    DivideBy: stripe.Int64(100),
    Round: stripe.String(stripe.PriceTransformQuantityRoundUp),
  },
  Recurring: &stripe.PriceCreateRecurringParams{
    UsageType: stripe.String(stripe.PriceRecurringUsageTypeMetered),
    Interval: stripe.String(stripe.PriceRecurringIntervalMonth),
    Meter: stripe.String("{{METER_ID}}"),
  },
  ProductData: &stripe.PriceCreateProductDataParams{
    Name: stripe.String("Hypernian tokens"),
  },
}
result, err := sc.V1Prices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PriceCreateOptions
{
    Currency = "usd",
    UnitAmount = 4,
    BillingScheme = "per_unit",
    TransformQuantity = new PriceTransformQuantityOptions
    {
        DivideBy = 100,
        Round = "up",
    },
    Recurring = new PriceRecurringOptions
    {
        UsageType = "metered",
        Interval = "month",
        Meter = "{{METER_ID}}",
    },
    ProductData = new PriceProductDataOptions { Name = "Hypernian tokens" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Prices;
Price price = service.Create(options);
```

## Create a customer

Next, create a *customer* (Customer objects represent customers of your business. They let you reuse payment methods and give you the ability to track multiple payments).

#### Dashboard

1. On the [Customers](https://dashboard.stripe.com/test/customers) page, click **Add customer**.
1. On the **Create customer** page, do the following:
   - For **Name**, enter the name of your customer. For the Hypernian example, enter `John Doe`.
   - (Optional) Add an email address and description for your customer.
   - Click **Add customer**.

#### API

```curl
curl https://api.stripe.com/v1/customers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d name="John Doe"
```

```cli
stripe customers create  \
  --name="John Doe"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

customer = client.v1.customers.create({name: 'John Doe'})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
customer = client.v1.customers.create({"name": "John Doe"})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$customer = $stripe->customers->create(['name' => 'John Doe']);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CustomerCreateParams params = CustomerCreateParams.builder().setName("John Doe").build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Customer customer = client.v1().customers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const customer = await stripe.customers.create({
  name: 'John Doe',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CustomerCreateParams{Name: stripe.String("John Doe")}
result, err := sc.V1Customers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CustomerCreateOptions { Name = "John Doe" };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Customers;
Customer customer = service.Create(options);
```

## Create a subscription

[Subscriptions](https://docs.stripe.com/api/subscriptions.md) allow you to charge recurring amounts by associating a customer with a specific price.

Use the Stripe Dashboard or API to create a subscription that includes your customer, product, and usage-based price.

For the Hypernian example, you create a subscription for the Hypernian product, with a metered price of 0.04 USD per 100 units, billed monthly to John Doe.

> You can associate a single metered price with one or more subscriptions.
> 
> When you create a `billing_mode=flexible` subscription, Stripe excludes metered line items from the first invoice since no prior usage exists to bill. Stripe creates an invoice only if the subscription is backdated with previously accrued usage or if pending invoice items exist. When you create a `billing_mode=classic` subscription, Stripe generates a zero monetary value invoice line item for each metered subscription item.

#### Dashboard

1. On the [Subscriptions](https://dashboard.stripe.com/test/subscriptions) page, click **Create test subscription**.
1. On the **Create a test subscription** page, do the following:
   - Under **Customer**, select the name of your customer. For the Hypernian example, select **John Doe**.
   - Under **Product**, select your price. For the Hypernian example, select the price under **Hypernian**.
   - (Optional) Modify the subscription details and settings as needed.
   - Click **Create test subscription**.

#### API

You can locate your customer ID on the customer details page. To locate your price ID, go to the product details page and click the overflow menu (?? under **Pricing**. Select **Copy price ID**.

```curl
curl https://api.stripe.com/v1/subscriptions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer={{CUSTOMER_ID}} \
  -d "items[0][price]"={{PRICE_ID}}
```

```cli
stripe subscriptions create  \
  --customer={{CUSTOMER_ID}} \
  -d "items[0][price]"={{PRICE_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

subscription = client.v1.subscriptions.create({
  customer: '{{CUSTOMER_ID}}',
  items: [{price: '{{PRICE_ID}}'}],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
subscription = client.v1.subscriptions.create({
  "customer": "{{CUSTOMER_ID}}",
  "items": [{"price": "{{PRICE_ID}}"}],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$subscription = $stripe->subscriptions->create([
  'customer' => '{{CUSTOMER_ID}}',
  'items' => [['price' => '{{PRICE_ID}}']],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SubscriptionCreateParams params =
  SubscriptionCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .addItem(SubscriptionCreateParams.Item.builder().setPrice("{{PRICE_ID}}").build())
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
      price: '{{PRICE_ID}}',
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
    &stripe.SubscriptionCreateItemParams{Price: stripe.String("{{PRICE_ID}}")},
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
        new SubscriptionItemOptions { Price = "{{PRICE_ID}}" },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Subscriptions;
Subscription subscription = service.Create(options);
```

## Send a test meter event

Use [Meter Events](https://docs.stripe.com/api/billing/meter-event.md) to [record customer usage](https://docs.stripe.com/billing/subscriptions/usage-based/recording-usage.md) for your meter. At the end of the billing period, Stripe bills the reported usage.

You can test your usage-based billing by sending a meter event through the Stripe Dashboard or API. When using the API, specify the customer ID and value for the `payload`.

After you send meter events, you can view usage details for your meter on the [Meters](https://dashboard.stripe.com/test/meters) page in the Dashboard.

#### Dashboard

1. On the [Meters](https://dashboard.stripe.com/test/meters) page, select the meter name. For the Hypernian example, select **Hypernian tokens**.
1. On the meter page, click **Add usage** > **Manually input usage**.
1. On the **Add usage** page, do the following:
   - Select your customer from the **Customer** dropdown.
   - For **Value**, enter a sample value. For the Hypernian example, enter `3000`.
   - Click **Submit**.

#### API

```curl
curl https://api.stripe.com/v1/billing/meter_events \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d event_name=hypernian_tokens \
  -d "payload[stripe_customer_id]"={{CUSTOMER_ID}} \
  -d "payload[value]"=25
```

```cli
stripe billing meter_events create  \
  --event-name=hypernian_tokens \
  -d "payload[stripe_customer_id]"={{CUSTOMER_ID}} \
  -d "payload[value]"=25
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

meter_event = client.v1.billing.meter_events.create({
  event_name: 'hypernian_tokens',
  payload: {
    stripe_customer_id: '{{CUSTOMER_ID}}',
    value: '25',
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
meter_event = client.v1.billing.meter_events.create({
  "event_name": "hypernian_tokens",
  "payload": {"stripe_customer_id": "{{CUSTOMER_ID}}", "value": "25"},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$meterEvent = $stripe->billing->meterEvents->create([
  'event_name' => 'hypernian_tokens',
  'payload' => [
    'stripe_customer_id' => '{{CUSTOMER_ID}}',
    'value' => '25',
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

MeterEventCreateParams params =
  MeterEventCreateParams.builder()
    .setEventName("hypernian_tokens")
    .putPayload("stripe_customer_id", "{{CUSTOMER_ID}}")
    .putPayload("value", "25")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
MeterEvent meterEvent = client.v1().billing().meterEvents().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const meterEvent = await stripe.billing.meterEvents.create({
  event_name: 'hypernian_tokens',
  payload: {
    stripe_customer_id: '{{CUSTOMER_ID}}',
    value: '25',
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.BillingMeterEventCreateParams{
  EventName: stripe.String("hypernian_tokens"),
  Payload: map[string]string{"stripe_customer_id": "{{CUSTOMER_ID}}", "value": "25"},
}
result, err := sc.V1BillingMeterEvents.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Billing.MeterEventCreateOptions
{
    EventName = "hypernian_tokens",
    Payload = new Dictionary<string, string>
    {
        { "stripe_customer_id", "{{CUSTOMER_ID}}" },
        { "value", "25" },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Billing.MeterEvents;
Stripe.Billing.MeterEvent meterEvent = service.Create(options);
```

## Create a preview invoice

[Create a preview invoice](https://docs.stripe.com/api/invoices/create_preview.md) to see a preview of a customer?™s invoice that includes details such as the meter price and usage quantity.

#### Dashboard

1. On the [Subscriptions](https://dashboard.stripe.com/test/subscriptions) page, select a subscription. For the Hypernian example, select the subscription for **John Doe**.

1. On the subscription details page, scroll down to the **Upcoming invoice** section. The preview invoice shows the subscription amount to bill the customer on the specified date.

1. Click **View full invoice** to see complete details for the upcoming invoice, including:

   - Customer
   - Billing method
   - Creation date
   - Connected subscription
   - Subscription details (usage quantity and meter price)
   - Amount due

   Because Stripe processes meter events asynchronously, upcoming invoices might not immediately reflect recently received meter events.

#### API

```curl
curl https://api.stripe.com/v1/invoices/create_preview \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d subscription="{{SUBSCRIPTION_ID}}"
```

```cli
stripe invoices create_preview  \
  --subscription="{{SUBSCRIPTION_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.create_preview({subscription: '{{SUBSCRIPTION_ID}}'})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.create_preview({
  "subscription": "{{SUBSCRIPTION_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->createPreview([
  'subscription' => '{{SUBSCRIPTION_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceCreatePreviewParams params =
  InvoiceCreatePreviewParams.builder()
    .setSubscription("{{SUBSCRIPTION_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Invoice invoice = client.v1().invoices().createPreview(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const invoice = await stripe.invoices.createPreview({
  subscription: '{{SUBSCRIPTION_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceCreatePreviewParams{
  Subscription: stripe.String("{{SUBSCRIPTION_ID}}"),
}
result, err := sc.V1Invoices.CreatePreview(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceCreatePreviewOptions
{
    Subscription = "{{SUBSCRIPTION_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.CreatePreview(options);
```

## Optional: Retrieve usage for a custom time period

Use the [Meter Event Summary](https://docs.stripe.com/api/billing/meter-event-summary.md) to retrieve total usage for a custom time period. The meter event summary returns a customer?™s aggregated usage for a period, based on the aggregation formula defined by the meter.

In the Hypernian example, the meter event summary returns the sum of tokens for a specific customer, meter, and time window.

Because Stripe processes meter events asynchronously, meter event summaries might not immediately reflect recently received meter events.

```curl
curl -G https://api.stripe.com/v1/billing/meters/{{METER_ID}}/event_summaries \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer={{CUSTOMER_ID}} \
  -d start_time=1717249380 \
  -d end_time=1717249440
```

```cli
stripe billing meter_event_summaries list {{METER_ID}} \
  --customer={{CUSTOMER_ID}} \
  --start-time=1717249380 \
  --end-time=1717249440
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

meter_event_summaries = client.v1.billing.meters.event_summaries.list(
  '{{METER_ID}}',
  {
    customer: '{{CUSTOMER_ID}}',
    start_time: 1717249380,
    end_time: 1717249440,
  },
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
meter_event_summaries = client.v1.billing.meters.event_summaries.list(
  "{{METER_ID}}",
  {"customer": "{{CUSTOMER_ID}}", "start_time": 1717249380, "end_time": 1717249440},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$meterEventSummaries = $stripe->billing->meters->allEventSummaries(
  '{{METER_ID}}',
  [
    'customer' => '{{CUSTOMER_ID}}',
    'start_time' => 1717249380,
    'end_time' => 1717249440,
  ]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

MeterEventSummaryListParams params =
  MeterEventSummaryListParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .setStartTime(1717249380L)
    .setEndTime(1717249440L)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
StripeCollection<MeterEventSummary> stripeCollection =
  client.v1().billing().meters().eventSummaries().list("{{METER_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const meterEventSummaries = await stripe.billing.meters.listEventSummaries(
  '{{METER_ID}}',
  {
    customer: '{{CUSTOMER_ID}}',
    start_time: 1717249380,
    end_time: 1717249440,
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.BillingMeterEventSummaryListParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  StartTime: stripe.Int64(1717249380),
  EndTime: stripe.Int64(1717249440),
  ID: stripe.String("{{METER_ID}}"),
}
result := sc.V1BillingMeterEventSummaries.List(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Billing.MeterEventSummaryListOptions
{
    Customer = "{{CUSTOMER_ID}}",
    StartTime = DateTimeOffset.FromUnixTimeSeconds(1717249380).UtcDateTime,
    EndTime = DateTimeOffset.FromUnixTimeSeconds(1717249440).UtcDateTime,
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Billing.Meters.EventSummaries;
StripeList<Stripe.Billing.MeterEventSummary> meterEventSummaries = service.List(
    "{{METER_ID}}",
    options);
```

## Next steps

- [Accept payments with Stripe Checkout](https://docs.stripe.com/payments/checkout.md)
- [Set up alerts and thresholds](https://docs.stripe.com/billing/subscriptions/usage-based/monitor.md)
- [Set up billing credits](https://docs.stripe.com/billing/subscriptions/usage-based/billing-credits/implementation-guide.md)
