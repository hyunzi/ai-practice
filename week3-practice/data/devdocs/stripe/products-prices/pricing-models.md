# Recurring pricing models

Learn about the pricing models you can use with subscriptions.

Pricing models are patterns that represent your business on Stripe and consists of the products or services you sell, how much they cost, what currency you accept for payments, and the service period for subscriptions. To build the pricing model, you use [products](https://docs.stripe.com/api/products.md) (what you sell) and [prices](https://docs.stripe.com/api/prices.md) (how much and how often to charge for your products).

| Pricing model                                                                                | Description                                                                                                                                                      |
| -------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| [Flat rate](https://docs.stripe.com/products-prices/pricing-models.md#flat-rate)             | Customers choose a service tier (for example, Basic, Starter, or Enterprise) and pay a flat rate for it.                                                         |
| [Per-seat](https://docs.stripe.com/products-prices/pricing-models.md#per-seat)               | Each pricing unit represents one user. For example, a business purchases software for its employees and each employee requires a license to access the software. |
| [Tiered](https://docs.stripe.com/products-prices/pricing-models.md#tiered-pricing)           | The unit cost changes with quantity (volume-based pricing) or usage (graduated pricing).                                                                         |
| [Usage-based](https://docs.stripe.com/products-prices/pricing-models.md#usage-based-pricing) | Charge customers based on their usage of your product or service. It includes fixed fee and overage, pay as you go, and credit burndown pricing models.          |

## Flat-rate pricing 

Many SaaS businesses offer their customers a choice of escalating service options. Customers choose a service tier and pay a flat rate for it. Imagine a business called [Typographic](https://typographic.io/) that sells a subscription webfont service. They offer three different service levels: Basic, Starter, and Enterprise. For each service level, they offer a monthly and yearly price.
![](https://b.stripecdn.com/docs-statics-srv/assets/pricing_model-flat-rate.e99989aa8c2abe76edd607462840776e.png)

Flat-rate pricing model

In this example, Typographic has three products: `Basic`, `Starter`, and `Enterprise`. Each product has several different prices. The basic level has prices for 10 USD per month and 100 USD per year. Both prices are for the same `Basic` product, so they share the same product description on the customer?�s receipt and invoice.

#### Dashboard

First, create the `Basic` product. To learn about all the options for creating a product, see the [prices guide](https://docs.stripe.com/products-prices/manage-prices.md#create-product).

1. Go to [Product catalog](https://dashboard.stripe.com/products).
1. Click **+ Create product**.
1. Enter a **Name** for the product.
1. (Optional) Add a **Description**. The description appears at checkout, on the [customer portal](https://docs.stripe.com/customer-management.md), and in [quotes](https://docs.stripe.com/quotes.md).

Next, create the monthly price for the `Basic` product:

1. Click **More pricing options**.
1. Select **Recurring**.
1. For **Choose your pricing model**, select **Flat rate**.
1. For **Amount**, enter a price amount.
1. For **Billing period**, select **Monthly**.
1. Click **Next** to save the price.

1. For **Pricing model**, select **Standard pricing**.
1. Select **Recurring**.
1. For **Amount**, enter a price amount.
1. For **Billing period**, select **Monthly**.

Then, create the yearly price for the `Basic` product:

1. Click **+ Add another price**.
1. Select **Recurring**.
1. For **Choose your pricing model**, select **Flat rate**.
1. For **Amount**, enter a price amount.
1. For **Billing period**, select **Yearly**.
1. Click **Next**.
1. Click **Add product** to save the product and price. You can only edit the product and price until you create a subscription with them.

1. Click **+ Add another price**.
1. For the **Pricing model**, select **Standard pricing**.
1. Select **Recurring**.
1. For **Amount**, enter a price amount.
1. For **Billing period**, select **Yearly**.
1. Click **Add product** to create the product and prices. You can only edit the product and price until you create a subscription with them.

#### API

1. Create a Product for the `Basic` service level.
   ```curl
   curl https://api.stripe.com/v1/products \
     -u "<<YOUR_SECRET_KEY>>:" \
     -d name=Basic
   ```

   ```cli
   stripe products create  \
     --name=Basic
   ```

   ```ruby
   # Set your secret key. Remember to switch to your live secret key in production.
   # See your keys here: https://dashboard.stripe.com/apikeys
   client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")
   
   product = client.v1.products.create({name: 'Basic'})
   ```

   ```python
   # Set your secret key. Remember to switch to your live secret key in production.
   # See your keys here: https://dashboard.stripe.com/apikeys
   client = StripeClient("<<YOUR_SECRET_KEY>>")
   
   # For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
   product = client.v1.products.create({"name": "Basic"})
   ```

   ```php
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   $stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');
   
   $product = $stripe->products->create(['name' => 'Basic']);
   ```

   ```java
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");
   
   ProductCreateParams params = ProductCreateParams.builder().setName("Basic").build();
   
   // For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
   Product product = client.v1().products().create(params);
   ```

   ```node
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');
   
   const product = await stripe.products.create({
     name: 'Basic',
   });
   ```

   ```go
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
   params := &stripe.ProductCreateParams{Name: stripe.String("Basic")}
   result, err := sc.V1Products.Create(context.TODO(), params)
   ```

   ```dotnet
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   var options = new ProductCreateOptions { Name = "Basic" };
   var client = new StripeClient("<<YOUR_SECRET_KEY>>");
   var service = client.V1.Products;
   Product product = service.Create(options);
   ```
1. Create the monthly price for the `Basic` product.
   ```curl
   curl https://api.stripe.com/v1/prices \
     -u "<<YOUR_SECRET_KEY>>:" \
     -d product="{{PRODUCT_ID}}" \
     -d unit_amount=1000 \
     -d currency=usd \
     -d "recurring[interval]"=month
   ```

   ```cli
   stripe prices create  \
     --product="{{PRODUCT_ID}}" \
     --unit-amount=1000 \
     --currency=usd \
     -d "recurring[interval]"=month
   ```

   ```ruby
   # Set your secret key. Remember to switch to your live secret key in production.
   # See your keys here: https://dashboard.stripe.com/apikeys
   client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")
   
   price = client.v1.prices.create({
     product: '{{PRODUCT_ID}}',
     unit_amount: 1000,
     currency: 'usd',
     recurring: {interval: 'month'},
   })
   ```

   ```python
   # Set your secret key. Remember to switch to your live secret key in production.
   # See your keys here: https://dashboard.stripe.com/apikeys
   client = StripeClient("<<YOUR_SECRET_KEY>>")
   
   # For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
   price = client.v1.prices.create({
     "product": "{{PRODUCT_ID}}",
     "unit_amount": 1000,
     "currency": "usd",
     "recurring": {"interval": "month"},
   })
   ```

   ```php
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   $stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');
   
   $price = $stripe->prices->create([
     'product' => '{{PRODUCT_ID}}',
     'unit_amount' => 1000,
     'currency' => 'usd',
     'recurring' => ['interval' => 'month'],
   ]);
   ```

   ```java
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");
   
   PriceCreateParams params =
     PriceCreateParams.builder()
       .setProduct("{{PRODUCT_ID}}")
       .setUnitAmount(1000L)
       .setCurrency("usd")
       .setRecurring(
         PriceCreateParams.Recurring.builder()
           .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
           .build()
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
     product: '{{PRODUCT_ID}}',
     unit_amount: 1000,
     currency: 'usd',
     recurring: {
       interval: 'month',
     },
   });
   ```

   ```go
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
   params := &stripe.PriceCreateParams{
     Product: stripe.String("{{PRODUCT_ID}}"),
     UnitAmount: stripe.Int64(1000),
     Currency: stripe.String(stripe.CurrencyUSD),
     Recurring: &stripe.PriceCreateRecurringParams{
       Interval: stripe.String(stripe.PriceRecurringIntervalMonth),
     },
   }
   result, err := sc.V1Prices.Create(context.TODO(), params)
   ```

   ```dotnet
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   var options = new PriceCreateOptions
   {
       Product = "{{PRODUCT_ID}}",
       UnitAmount = 1000,
       Currency = "usd",
       Recurring = new PriceRecurringOptions { Interval = "month" },
   };
   var client = new StripeClient("<<YOUR_SECRET_KEY>>");
   var service = client.V1.Prices;
   Price price = service.Create(options);
   ```
1. Create the yearly price for the `Basic` product.
   ```curl
   curl https://api.stripe.com/v1/prices \
     -u "<<YOUR_SECRET_KEY>>:" \
     -d product="{{PRODUCT_ID}}" \
     -d unit_amount=10000 \
     -d currency=usd \
     -d "recurring[interval]"=year
   ```

   ```cli
   stripe prices create  \
     --product="{{PRODUCT_ID}}" \
     --unit-amount=10000 \
     --currency=usd \
     -d "recurring[interval]"=year
   ```

   ```ruby
   # Set your secret key. Remember to switch to your live secret key in production.
   # See your keys here: https://dashboard.stripe.com/apikeys
   client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")
   
   price = client.v1.prices.create({
     product: '{{PRODUCT_ID}}',
     unit_amount: 10000,
     currency: 'usd',
     recurring: {interval: 'year'},
   })
   ```

   ```python
   # Set your secret key. Remember to switch to your live secret key in production.
   # See your keys here: https://dashboard.stripe.com/apikeys
   client = StripeClient("<<YOUR_SECRET_KEY>>")
   
   # For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
   price = client.v1.prices.create({
     "product": "{{PRODUCT_ID}}",
     "unit_amount": 10000,
     "currency": "usd",
     "recurring": {"interval": "year"},
   })
   ```

   ```php
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   $stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');
   
   $price = $stripe->prices->create([
     'product' => '{{PRODUCT_ID}}',
     'unit_amount' => 10000,
     'currency' => 'usd',
     'recurring' => ['interval' => 'year'],
   ]);
   ```

   ```java
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");
   
   PriceCreateParams params =
     PriceCreateParams.builder()
       .setProduct("{{PRODUCT_ID}}")
       .setUnitAmount(10000L)
       .setCurrency("usd")
       .setRecurring(
         PriceCreateParams.Recurring.builder()
           .setInterval(PriceCreateParams.Recurring.Interval.YEAR)
           .build()
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
     product: '{{PRODUCT_ID}}',
     unit_amount: 10000,
     currency: 'usd',
     recurring: {
       interval: 'year',
     },
   });
   ```

   ```go
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
   params := &stripe.PriceCreateParams{
     Product: stripe.String("{{PRODUCT_ID}}"),
     UnitAmount: stripe.Int64(10000),
     Currency: stripe.String(stripe.CurrencyUSD),
     Recurring: &stripe.PriceCreateRecurringParams{
       Interval: stripe.String(stripe.PriceRecurringIntervalYear),
     },
   }
   result, err := sc.V1Prices.Create(context.TODO(), params)
   ```

   ```dotnet
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   var options = new PriceCreateOptions
   {
       Product = "{{PRODUCT_ID}}",
       UnitAmount = 10000,
       Currency = "usd",
       Recurring = new PriceRecurringOptions { Interval = "year" },
   };
   var client = new StripeClient("<<YOUR_SECRET_KEY>>");
   var service = client.V1.Prices;
   Price price = service.Create(options);
   ```

Repeat these steps to create the `Starter` and `Enterprise` products and their associated prices. After you create this pricing model, you?�re ready to use them to create [subscriptions](https://docs.stripe.com/api/subscriptions.md) for your customers.

```curl
curl https://api.stripe.com/v1/subscriptions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "items[0][price]"={{RECURRING_PRICE_ID}}
```

```cli
stripe subscriptions create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "items[0][price]"={{RECURRING_PRICE_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

subscription = client.v1.subscriptions.create({
  customer: '{{CUSTOMER_ID}}',
  items: [{price: '{{RECURRING_PRICE_ID}}'}],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
subscription = client.v1.subscriptions.create({
  "customer": "{{CUSTOMER_ID}}",
  "items": [{"price": "{{RECURRING_PRICE_ID}}"}],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$subscription = $stripe->subscriptions->create([
  'customer' => '{{CUSTOMER_ID}}',
  'items' => [['price' => '{{RECURRING_PRICE_ID}}']],
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
      SubscriptionCreateParams.Item.builder().setPrice("{{RECURRING_PRICE_ID}}").build()
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
      price: '{{RECURRING_PRICE_ID}}',
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
    &stripe.SubscriptionCreateItemParams{Price: stripe.String("{{RECURRING_PRICE_ID}}")},
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
        new SubscriptionItemOptions { Price = "{{RECURRING_PRICE_ID}}" },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Subscriptions;
Subscription subscription = service.Create(options);
```

### Per-seat pricing 

Per-seat pricing is a linear pricing model where the number of seats (for example, software licenses) maps to the number of units (for example, users). Typographic, our example company, also wants to offer a per-seat plan. Typographic?�s customers pick how many seats they?�ll use, and Typographic charges based on that amount.
![](https://b.stripecdn.com/docs-statics-srv/assets/pricing_model-per-seat.8ed5ad9243ad6ae1c38b072cbb4ce07a.png)

Per-seat pricing model

To model this scenario, Typographic creates a product with a flat-rate price where each unit represents a user. When Typographic creates a subscription for a customer, the customer specifies the number of users for that subscription.

#### Dashboard

First, create the per-seat product. To learn about all the options for creating a product, see the [prices guide](https://docs.stripe.com/products-prices/manage-prices.md#create-product).

1. Go to [Product catalog](https://dashboard.stripe.com/products).
1. Click **+ Create product**.
1. Enter a **Name** for the product.
1. (Optional) Add a **Description**. The description appears at checkout, on the [customer portal](https://docs.stripe.com/customer-management.md), and in [quotes](https://docs.stripe.com/quotes.md).

Next, create the monthly price for the product:

1. Select **Recurring**.
1. For **Amount**, enter a price amount.
1. For **Billing period**, select **Monthly**.
1. Click **Add product** to save the product and price. You can only edit the product and price until you create a subscription with them.

1. Select **Standard pricing** for the **Pricing model**, then select **Recurring**.
1. For **Amount**, enter a price amount.
1. For **Billing period**, select **Monthly**.
1. Click **Add product** to save the product and price. You can only edit the product and price until you create a subscription with them.

To create a subscription using that price:

1. Go to the **Billing** > **Subscriptions** page.
1. Click **+ Create subscription**.
1. Find or add a customer.
1. Search for the product you created and select the price you want to use.
1. (Optional) Select **Collect tax automatically** to use Stripe Tax.
1. Click **Start subscription** to start it immmediately or **Schedule subscription** to start it on another schedule.

#### API

1. Create the per-seat product.
   ```curl
   curl https://api.stripe.com/v1/products \
     -u "<<YOUR_SECRET_KEY>>:" \
     -d name=Per-seat
   ```

   ```cli
   stripe products create  \
     --name=Per-seat
   ```

   ```ruby
   # Set your secret key. Remember to switch to your live secret key in production.
   # See your keys here: https://dashboard.stripe.com/apikeys
   client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")
   
   product = client.v1.products.create({name: 'Per-seat'})
   ```

   ```python
   # Set your secret key. Remember to switch to your live secret key in production.
   # See your keys here: https://dashboard.stripe.com/apikeys
   client = StripeClient("<<YOUR_SECRET_KEY>>")
   
   # For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
   product = client.v1.products.create({"name": "Per-seat"})
   ```

   ```php
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   $stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');
   
   $product = $stripe->products->create(['name' => 'Per-seat']);
   ```

   ```java
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");
   
   ProductCreateParams params = ProductCreateParams.builder().setName("Per-seat").build();
   
   // For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
   Product product = client.v1().products().create(params);
   ```

   ```node
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');
   
   const product = await stripe.products.create({
     name: 'Per-seat',
   });
   ```

   ```go
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
   params := &stripe.ProductCreateParams{Name: stripe.String("Per-seat")}
   result, err := sc.V1Products.Create(context.TODO(), params)
   ```

   ```dotnet
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   var options = new ProductCreateOptions { Name = "Per-seat" };
   var client = new StripeClient("<<YOUR_SECRET_KEY>>");
   var service = client.V1.Products;
   Product product = service.Create(options);
   ```
1. Create a price for the monthly fee.
   ```curl
   curl https://api.stripe.com/v1/prices \
     -u "<<YOUR_SECRET_KEY>>:" \
     -d product="{{PRODUCT_ID}}" \
     -d unit_amount=1000 \
     -d currency=usd \
     -d "recurring[interval]"=month
   ```

   ```cli
   stripe prices create  \
     --product="{{PRODUCT_ID}}" \
     --unit-amount=1000 \
     --currency=usd \
     -d "recurring[interval]"=month
   ```

   ```ruby
   # Set your secret key. Remember to switch to your live secret key in production.
   # See your keys here: https://dashboard.stripe.com/apikeys
   client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")
   
   price = client.v1.prices.create({
     product: '{{PRODUCT_ID}}',
     unit_amount: 1000,
     currency: 'usd',
     recurring: {interval: 'month'},
   })
   ```

   ```python
   # Set your secret key. Remember to switch to your live secret key in production.
   # See your keys here: https://dashboard.stripe.com/apikeys
   client = StripeClient("<<YOUR_SECRET_KEY>>")
   
   # For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
   price = client.v1.prices.create({
     "product": "{{PRODUCT_ID}}",
     "unit_amount": 1000,
     "currency": "usd",
     "recurring": {"interval": "month"},
   })
   ```

   ```php
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   $stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');
   
   $price = $stripe->prices->create([
     'product' => '{{PRODUCT_ID}}',
     'unit_amount' => 1000,
     'currency' => 'usd',
     'recurring' => ['interval' => 'month'],
   ]);
   ```

   ```java
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");
   
   PriceCreateParams params =
     PriceCreateParams.builder()
       .setProduct("{{PRODUCT_ID}}")
       .setUnitAmount(1000L)
       .setCurrency("usd")
       .setRecurring(
         PriceCreateParams.Recurring.builder()
           .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
           .build()
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
     product: '{{PRODUCT_ID}}',
     unit_amount: 1000,
     currency: 'usd',
     recurring: {
       interval: 'month',
     },
   });
   ```

   ```go
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
   params := &stripe.PriceCreateParams{
     Product: stripe.String("{{PRODUCT_ID}}"),
     UnitAmount: stripe.Int64(1000),
     Currency: stripe.String(stripe.CurrencyUSD),
     Recurring: &stripe.PriceCreateRecurringParams{
       Interval: stripe.String(stripe.PriceRecurringIntervalMonth),
     },
   }
   result, err := sc.V1Prices.Create(context.TODO(), params)
   ```

   ```dotnet
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   var options = new PriceCreateOptions
   {
       Product = "{{PRODUCT_ID}}",
       UnitAmount = 1000,
       Currency = "usd",
       Recurring = new PriceRecurringOptions { Interval = "month" },
   };
   var client = new StripeClient("<<YOUR_SECRET_KEY>>");
   var service = client.V1.Prices;
   Price price = service.Create(options);
   ```
1. When you create the [subscription](https://docs.stripe.com/api/subscriptions.md), specify a `quantity` to charge for the number of seats.
   ```curl
   curl https://api.stripe.com/v1/subscriptions \
     -u "<<YOUR_SECRET_KEY>>:" \
     -d customer="{{CUSTOMER_ID}}" \
     -d "items[0][price]"={{per_seat_price_id}} \
     -d "items[0][quantity]"=12
   ```

   ```cli
   stripe subscriptions create  \
     --customer="{{CUSTOMER_ID}}" \
     -d "items[0][price]"={{per_seat_price_id}} \
     -d "items[0][quantity]"=12
   ```

   ```ruby
   # Set your secret key. Remember to switch to your live secret key in production.
   # See your keys here: https://dashboard.stripe.com/apikeys
   client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")
   
   subscription = client.v1.subscriptions.create({
     customer: '{{CUSTOMER_ID}}',
     items: [
       {
         price: '{{per_seat_price_id}}',
         quantity: 12,
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
     "items": [{"price": "{{per_seat_price_id}}", "quantity": 12}],
   })
   ```

   ```php
   // Set your secret key. Remember to switch to your live secret key in production.
   // See your keys here: https://dashboard.stripe.com/apikeys
   $stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');
   
   $subscription = $stripe->subscriptions->create([
     'customer' => '{{CUSTOMER_ID}}',
     'items' => [
       [
         'price' => '{{per_seat_price_id}}',
         'quantity' => 12,
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
         SubscriptionCreateParams.Item.builder()
           .setPrice("{{per_seat_price_id}}")
           .setQuantity(12L)
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
         price: '{{per_seat_price_id}}',
         quantity: 12,
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
       &stripe.SubscriptionCreateItemParams{
         Price: stripe.String("{{per_seat_price_id}}"),
         Quantity: stripe.Int64(12),
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
           new SubscriptionItemOptions { Price = "{{per_seat_price_id}}", Quantity = 12 },
       },
   };
   var client = new StripeClient("<<YOUR_SECRET_KEY>>");
   var service = client.V1.Subscriptions;
   Subscription subscription = service.Create(options);
   ```

## Tiered pricing 

Prices can represent tiers, allowing the unit cost to change with quantity or usage. Use tiers if you need non-linear pricing when `quantity` or [usage](https://docs.stripe.com/api/billing/meter-event.md) changes. You can also combine tiered pricing with base fees to create [usage-based pricing models](https://docs.stripe.com/products-prices/pricing-models.md#usage-based-pricing).

For example, Typographic wants to offer lower rates for customers who use more fonts per month. The following tiered pricing models show two different ways to adjust pricing as usage increases: volume-based pricing and graduated pricing. To demonstrate these approaches to tiered pricing, we?�ll use the following tiers:

|             | Number of fonts | Price per tier |
| ----------- | --------------- | -------------- |
| First tier  | 1-5             | 7 USD          |
| Second tier | 6-10            | 6.5 USD        |
| Third tier  | 11+             | 6 USD          |

### Volume-based pricing 

With volume-based pricing, the subscription item bills at the tier corresponding to the amount of usage at the end of the period. The entire `quantity`  (or `usage`) is multiplied by the unit cost of the tier. Because the tier price applies to the entire `quantity` (or `usage`), the total might decrease when calculating the final cost.

For example, a customer with 5 fonts is charged 35 USD (5 × 7 USD). If they use 6 fonts the following month, then all fonts bill at the `6-10` rate. That month, they?�re charged 39 USD (6 × 6.5 USD).

| Quantity and usage at end of the period | Unit cost | Total monthly cost |
| --------------------------------------- | --------- | ------------------ |
| 1                                       | 7 USD     | 7 USD              |
| 5                                       | 7 USD     | 35 USD             |
| 6                                       | 6.5 USD   | 39 USD             |
| 20                                      | 6 USD     | 120 USD            |
| 25                                      | 6 USD     | 150 USD            |

#### Dashboard

1. Go to the [Product catalog](https://dashboard.stripe.com/products).
1. Click **+ Create product**.
1. Enter a **Name** for the product.
1. (Optional) Add a **Description**. The description appears at checkout, on the [customer portal](https://docs.stripe.com/customer-management.md), and in [quotes](https://docs.stripe.com/quotes.md).

Next, create the monthly price for the product:

1. Click **More pricing options**.
1. Select **Recurring**.
1. For **Choose your pricing model**, select **Tiered pricing** and **Volume**.
1. Under **Price**, create three tiers:
|             | First unit | Last unit | Per unit | Flat rate |
| ----------- | ---------- | --------- | -------- | --------- |
| First tier  | 1          | 5         | 7 USD    | 0 USD     |
| Second tier | 6          | 10        | 6.5 USD  | 0 USD     |
| Third tier  | 11         | ??        | 6 USD    | 0 USD     |
1. For **Billing period**, select **Monthly**.
1. Click **Add product** to save the product and price. You can only edit the product and price until you create a subscription with them.

#### API

Create the tiers to match the example and set the value of `tiers_mode` to `volume`:

```curl
curl https://api.stripe.com/v1/prices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d nickname="Font Volume Pricing" \
  -d "tiers[0][unit_amount]"=700 \
  -d "tiers[0][up_to]"=5 \
  -d "tiers[1][unit_amount]"=650 \
  -d "tiers[1][up_to]"=10 \
  -d "tiers[2][unit_amount]"=600 \
  -d "tiers[2][up_to]"=inf \
  -d currency=usd \
  -d "recurring[interval]"=month \
  -d "recurring[usage_type]"=metered \
  -d product="{{PRODUCT_ID}}" \
  -d tiers_mode=volume \
  -d billing_scheme=tiered \
  -d "expand[0]"=tiers
```

```cli
stripe prices create  \
  --nickname="Font Volume Pricing" \
  -d "tiers[0][unit_amount]"=700 \
  -d "tiers[0][up_to]"=5 \
  -d "tiers[1][unit_amount]"=650 \
  -d "tiers[1][up_to]"=10 \
  -d "tiers[2][unit_amount]"=600 \
  -d "tiers[2][up_to]"=inf \
  --currency=usd \
  -d "recurring[interval]"=month \
  -d "recurring[usage_type]"=metered \
  --product="{{PRODUCT_ID}}" \
  --tiers-mode=volume \
  --billing-scheme=tiered \
  -d "expand[0]"=tiers
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

price = client.v1.prices.create({
  nickname: 'Font Volume Pricing',
  tiers: [
    {
      unit_amount: 700,
      up_to: 5,
    },
    {
      unit_amount: 650,
      up_to: 10,
    },
    {
      unit_amount: 600,
      up_to: 'inf',
    },
  ],
  currency: 'usd',
  recurring: {
    interval: 'month',
    usage_type: 'metered',
  },
  product: '{{PRODUCT_ID}}',
  tiers_mode: 'volume',
  billing_scheme: 'tiered',
  expand: ['tiers'],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
price = client.v1.prices.create({
  "nickname": "Font Volume Pricing",
  "tiers": [
    {"unit_amount": 700, "up_to": 5},
    {"unit_amount": 650, "up_to": 10},
    {"unit_amount": 600, "up_to": "inf"},
  ],
  "currency": "usd",
  "recurring": {"interval": "month", "usage_type": "metered"},
  "product": "{{PRODUCT_ID}}",
  "tiers_mode": "volume",
  "billing_scheme": "tiered",
  "expand": ["tiers"],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$price = $stripe->prices->create([
  'nickname' => 'Font Volume Pricing',
  'tiers' => [
    [
      'unit_amount' => 700,
      'up_to' => 5,
    ],
    [
      'unit_amount' => 650,
      'up_to' => 10,
    ],
    [
      'unit_amount' => 600,
      'up_to' => 'inf',
    ],
  ],
  'currency' => 'usd',
  'recurring' => [
    'interval' => 'month',
    'usage_type' => 'metered',
  ],
  'product' => '{{PRODUCT_ID}}',
  'tiers_mode' => 'volume',
  'billing_scheme' => 'tiered',
  'expand' => ['tiers'],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PriceCreateParams params =
  PriceCreateParams.builder()
    .setNickname("Font Volume Pricing")
    .addTier(PriceCreateParams.Tier.builder().setUnitAmount(700L).setUpTo(5L).build())
    .addTier(PriceCreateParams.Tier.builder().setUnitAmount(650L).setUpTo(10L).build())
    .addTier(
      PriceCreateParams.Tier.builder()
        .setUnitAmount(600L)
        .setUpTo(PriceCreateParams.Tier.UpTo.INF)
        .build()
    )
    .setCurrency("usd")
    .setRecurring(
      PriceCreateParams.Recurring.builder()
        .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
        .setUsageType(PriceCreateParams.Recurring.UsageType.METERED)
        .build()
    )
    .setProduct("{{PRODUCT_ID}}")
    .setTiersMode(PriceCreateParams.TiersMode.VOLUME)
    .setBillingScheme(PriceCreateParams.BillingScheme.TIERED)
    .addExpand("tiers")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Price price = client.v1().prices().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const price = await stripe.prices.create({
  nickname: 'Font Volume Pricing',
  tiers: [
    {
      unit_amount: 700,
      up_to: 5,
    },
    {
      unit_amount: 650,
      up_to: 10,
    },
    {
      unit_amount: 600,
      up_to: 'inf',
    },
  ],
  currency: 'usd',
  recurring: {
    interval: 'month',
    usage_type: 'metered',
  },
  product: '{{PRODUCT_ID}}',
  tiers_mode: 'volume',
  billing_scheme: 'tiered',
  expand: ['tiers'],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PriceCreateParams{
  Nickname: stripe.String("Font Volume Pricing"),
  Tiers: []*stripe.PriceCreateTierParams{
    &stripe.PriceCreateTierParams{UnitAmount: stripe.Int64(700), UpTo: stripe.Int64(5)},
    &stripe.PriceCreateTierParams{UnitAmount: stripe.Int64(650), UpTo: stripe.Int64(10)},
    &stripe.PriceCreateTierParams{
      UnitAmount: stripe.Int64(600),
      UpToInf: stripe.String("inf"),
    },
  },
  Currency: stripe.String(stripe.CurrencyUSD),
  Recurring: &stripe.PriceCreateRecurringParams{
    Interval: stripe.String(stripe.PriceRecurringIntervalMonth),
    UsageType: stripe.String(stripe.PriceRecurringUsageTypeMetered),
  },
  Product: stripe.String("{{PRODUCT_ID}}"),
  TiersMode: stripe.String(stripe.PriceTiersModeVolume),
  BillingScheme: stripe.String(stripe.PriceBillingSchemeTiered),
}
params.AddExpand("tiers")
result, err := sc.V1Prices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PriceCreateOptions
{
    Nickname = "Font Volume Pricing",
    Tiers = new List<PriceTierOptions>
    {
        new PriceTierOptions { UnitAmount = 700, UpTo = 5 },
        new PriceTierOptions { UnitAmount = 650, UpTo = 10 },
        new PriceTierOptions { UnitAmount = 600, UpTo = PriceTierUpTo.Inf },
    },
    Currency = "usd",
    Recurring = new PriceRecurringOptions { Interval = "month", UsageType = "metered" },
    Product = "{{PRODUCT_ID}}",
    TiersMode = "volume",
    BillingScheme = "tiered",
    Expand = new List<string> { "tiers" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Prices;
Price price = service.Create(options);
```

### Graduated pricing 

While similar to volume pricing, graduated pricing charges for the usage in each tier instead of applying a single price for overall usage. The `quantity` is multiplied by the amount for each tier and the totals for each tier are summed together.

For example, 5 fonts result in the same charge as volume-based pricing??5 USD total at 7 USD per font. This changes as usage exceeds the first tier. A customer with more than 5 fonts is charged 7 USD per font for the first 5 fonts, then 6.5 USD for fonts 6 through 10, and finally 6 USD per font thereafter. A customer with 6 fonts is charged 41.5 USD, 35 USD for the first 5 fonts and 6.5 USD for the 6th font.

| Quantity and usage at end of the period | Total for graduated tiered pricing |
| --------------------------------------- | ---------------------------------- |
| 1                                       | 7 USD                              |
| 5                                       | 35 USD                             |
| 6                                       | 41.5 USD                           |
| 20                                      | 127.5 USD                          |
| 25                                      | 157.5 USD                          |

#### Dashboard

1. Go to the [Product catalog](https://dashboard.stripe.com/products).
1. Click **+ Create product**.
1. Enter a **Name** for the product.
1. (Optional) Add a **Description**. The description appears at checkout, on the [customer portal](https://docs.stripe.com/customer-management.md), and in [quotes](https://docs.stripe.com/quotes.md).

Next, create the monthly price for the product:

1. Click **More pricing options**.
1. Select **Recurring**.
1. For **Choose your pricing model**, select **Tiered pricing** and **Graduated**.
1. Under **Price**, create three tiers:
|             | First unit | Last unit | Per unit | Flat rate |
| ----------- | ---------- | --------- | -------- | --------- |
| First tier  | 1          | 5         | 7 USD    | 0 USD     |
| Second tier | 6          | 10        | 6.5 USD  | 0 USD     |
| Third tier  | 11         | ??        | 6 USD    | 0 USD     |
1. For the **Billing period**, select **Monthly**.
1. Click **Add product** to save the product and price. You can only edit the product and price until you create a subscription with them.

#### API

Create the tiers to match the example and set the value of `tiers_mode` to `graduated`:

```curl
curl https://api.stripe.com/v1/prices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d nickname="Per-minute pricing" \
  -d "tiers[0][unit_amount]"=700 \
  -d "tiers[0][up_to]"=5 \
  -d "tiers[1][unit_amount]"=650 \
  -d "tiers[1][up_to]"=10 \
  -d "tiers[2][unit_amount]"=600 \
  -d "tiers[2][up_to]"=inf \
  -d currency=usd \
  -d "recurring[interval]"=month \
  -d "recurring[usage_type]"=metered \
  -d product="{{PRODUCT_ID}}" \
  -d tiers_mode=graduated \
  -d billing_scheme=tiered \
  -d "expand[0]"=tiers
```

```cli
stripe prices create  \
  --nickname="Per-minute pricing" \
  -d "tiers[0][unit_amount]"=700 \
  -d "tiers[0][up_to]"=5 \
  -d "tiers[1][unit_amount]"=650 \
  -d "tiers[1][up_to]"=10 \
  -d "tiers[2][unit_amount]"=600 \
  -d "tiers[2][up_to]"=inf \
  --currency=usd \
  -d "recurring[interval]"=month \
  -d "recurring[usage_type]"=metered \
  --product="{{PRODUCT_ID}}" \
  --tiers-mode=graduated \
  --billing-scheme=tiered \
  -d "expand[0]"=tiers
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

price = client.v1.prices.create({
  nickname: 'Per-minute pricing',
  tiers: [
    {
      unit_amount: 700,
      up_to: 5,
    },
    {
      unit_amount: 650,
      up_to: 10,
    },
    {
      unit_amount: 600,
      up_to: 'inf',
    },
  ],
  currency: 'usd',
  recurring: {
    interval: 'month',
    usage_type: 'metered',
  },
  product: '{{PRODUCT_ID}}',
  tiers_mode: 'graduated',
  billing_scheme: 'tiered',
  expand: ['tiers'],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
price = client.v1.prices.create({
  "nickname": "Per-minute pricing",
  "tiers": [
    {"unit_amount": 700, "up_to": 5},
    {"unit_amount": 650, "up_to": 10},
    {"unit_amount": 600, "up_to": "inf"},
  ],
  "currency": "usd",
  "recurring": {"interval": "month", "usage_type": "metered"},
  "product": "{{PRODUCT_ID}}",
  "tiers_mode": "graduated",
  "billing_scheme": "tiered",
  "expand": ["tiers"],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$price = $stripe->prices->create([
  'nickname' => 'Per-minute pricing',
  'tiers' => [
    [
      'unit_amount' => 700,
      'up_to' => 5,
    ],
    [
      'unit_amount' => 650,
      'up_to' => 10,
    ],
    [
      'unit_amount' => 600,
      'up_to' => 'inf',
    ],
  ],
  'currency' => 'usd',
  'recurring' => [
    'interval' => 'month',
    'usage_type' => 'metered',
  ],
  'product' => '{{PRODUCT_ID}}',
  'tiers_mode' => 'graduated',
  'billing_scheme' => 'tiered',
  'expand' => ['tiers'],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PriceCreateParams params =
  PriceCreateParams.builder()
    .setNickname("Per-minute pricing")
    .addTier(PriceCreateParams.Tier.builder().setUnitAmount(700L).setUpTo(5L).build())
    .addTier(PriceCreateParams.Tier.builder().setUnitAmount(650L).setUpTo(10L).build())
    .addTier(
      PriceCreateParams.Tier.builder()
        .setUnitAmount(600L)
        .setUpTo(PriceCreateParams.Tier.UpTo.INF)
        .build()
    )
    .setCurrency("usd")
    .setRecurring(
      PriceCreateParams.Recurring.builder()
        .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
        .setUsageType(PriceCreateParams.Recurring.UsageType.METERED)
        .build()
    )
    .setProduct("{{PRODUCT_ID}}")
    .setTiersMode(PriceCreateParams.TiersMode.GRADUATED)
    .setBillingScheme(PriceCreateParams.BillingScheme.TIERED)
    .addExpand("tiers")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Price price = client.v1().prices().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const price = await stripe.prices.create({
  nickname: 'Per-minute pricing',
  tiers: [
    {
      unit_amount: 700,
      up_to: 5,
    },
    {
      unit_amount: 650,
      up_to: 10,
    },
    {
      unit_amount: 600,
      up_to: 'inf',
    },
  ],
  currency: 'usd',
  recurring: {
    interval: 'month',
    usage_type: 'metered',
  },
  product: '{{PRODUCT_ID}}',
  tiers_mode: 'graduated',
  billing_scheme: 'tiered',
  expand: ['tiers'],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PriceCreateParams{
  Nickname: stripe.String("Per-minute pricing"),
  Tiers: []*stripe.PriceCreateTierParams{
    &stripe.PriceCreateTierParams{UnitAmount: stripe.Int64(700), UpTo: stripe.Int64(5)},
    &stripe.PriceCreateTierParams{UnitAmount: stripe.Int64(650), UpTo: stripe.Int64(10)},
    &stripe.PriceCreateTierParams{
      UnitAmount: stripe.Int64(600),
      UpToInf: stripe.String("inf"),
    },
  },
  Currency: stripe.String(stripe.CurrencyUSD),
  Recurring: &stripe.PriceCreateRecurringParams{
    Interval: stripe.String(stripe.PriceRecurringIntervalMonth),
    UsageType: stripe.String(stripe.PriceRecurringUsageTypeMetered),
  },
  Product: stripe.String("{{PRODUCT_ID}}"),
  TiersMode: stripe.String(stripe.PriceTiersModeGraduated),
  BillingScheme: stripe.String(stripe.PriceBillingSchemeTiered),
}
params.AddExpand("tiers")
result, err := sc.V1Prices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PriceCreateOptions
{
    Nickname = "Per-minute pricing",
    Tiers = new List<PriceTierOptions>
    {
        new PriceTierOptions { UnitAmount = 700, UpTo = 5 },
        new PriceTierOptions { UnitAmount = 650, UpTo = 10 },
        new PriceTierOptions { UnitAmount = 600, UpTo = PriceTierUpTo.Inf },
    },
    Currency = "usd",
    Recurring = new PriceRecurringOptions { Interval = "month", UsageType = "metered" },
    Product = "{{PRODUCT_ID}}",
    TiersMode = "graduated",
    BillingScheme = "tiered",
    Expand = new List<string> { "tiers" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Prices;
Price price = service.Create(options);
```

### Add a flat rate 

You can specify a flat rate (`flat_amount`) to add to the *invoice* (Invoices are statements of amounts owed by a customer. They track the status of payments from draft through paid or otherwise finalized. Subscriptions automatically generate invoices, or you can manually create a one-off invoice). This works for both volume and graduated pricing. For example, you can have a flat fee that increases when your customer exceeds certain usage thresholds:

| Tier               | Amount (unit cost)         | Flat rate                   |
| ------------------ | -------------------------- | --------------------------- |
| 1-5 (`up_to=5`)    | 5 USD (`unit_amount=500`)  | 10 USD (`flat_amount=1000`) |
| 6-10 (`up_to=10`)  | 4 USD  (`unit_amount=400`) | 20 USD (`flat_amount=2000`) |
| 10-15 (`up_to=15`) | 3 USD (`unit_amount=300`)  | 30 USD (`flat_amount=3000`) |
| 15-20 (`up_to=20`) | 2 USD (`unit_amount=200`)  | 40 USD (`flat_amount=4000`) |
| 20+ (`up_to=inf`)  | 1 USD (`unit_amount=100`)  | 50 USD (`flat_amount=5000`) |

#### Volume-based pricing flat rate example 

If `quantity` is `12` and `tiers_mode=volume`, the total amount billed is:

12 × 3 USD + 30 USD = 66 USD

#### Graduated pricing flat rate example 

If `quantity` is `12` and `tiers_mode=graduated`, the total amount billed is:

(5 × 5 USD + 10 USD) + (5 × 4 USD + 20 USD) + (2 × 3 USD + 30 USD) = 111 USD

A tier can have either a `unit_amount` or a `flat_amount`, or both, but it must have at least one of the two. If `quantity` is `0`, the total amount is 10 USD regardless of the tiered pricing model used. Stripe always bills the first flat rate tier when `quantity=0`. To bill `0` when there?�s no usage, set up an `up_to=1` tier with an `unit_amount` equal to the flat rate and omit the `flat_amount`.

## Usage-based pricing 

Usage-based pricing enables you to charge based on a customer?�s usage of your product or service. Usage-based pricing includes models such as fixed fee and overage, pay as you go, and credit burndown.

### Fixed fee and overage 

Use the fixed fee and overage model to charge a flat rate per month for your service at the beginning of the period. The flat rate has some included usage entitlement, and any additional usage (overage) charges at the end of the period.

You can use the Stripe Dashboard or API to set this up with two prices within the same product. For example, Hypernian introduces an advanced model called Hypernian. Priced at 200 USD per month, this model includes 100,000 tokens. They charge any usage above the included tokens at an additional rate of 0.001 USD per token.

#### Dashboard

1. On the [Product catalog](https://dashboard.stripe.com/test/products) page, click **Create product**.

1. On the **Add a product** page, do the following:

   - For **Name**, enter the name of your product. For the Hypernian example, enter ?�Hypernian.??   - (Optional) For **Description**, add a description that appears at checkout in the [customer portal](https://docs.stripe.com/customer-management.md) and in [quotes](https://docs.stripe.com/quotes.md).
   - Under **Billing period**, select **More pricing options**.

1. On the **Add price** page, do the following:

   - Under **Choose your pricing model**, select **Flat rate**.
   - Under **Price**, set the **Amount** to 200 USD.
   - Click **Next**

1. To add a second recurring price to the product, click **Add another price** on the **Add a product** page.

1. On the **Add price** page,do the following:

   - Under **Choose your pricing model**, select **Usage-based**, **Per tier**, and **Graduated**.

   - Under **Price**, create two graduated pricing tiers:

|             | First unit | Last unit | Per unit  | Flat rate |
| ----------- | ---------- | --------- | --------- | --------- |
| First tier  | 0          | 100,000   | 0 USD     | 0 USD     |
| Second tier | 100,001    | ??        | 0.001 USD | 0 USD     |

1. Under **Meter**, create a new meter to record usage. For the Hypernian example, use the meter name ?�hypernian_api_tokens.??
1. Click **Next**.

1. Click **Add product**. When you create subscriptions, specify both prices.

#### API

First, create your [product](https://docs.stripe.com/api/products.md). For the Hypernian example, use the name `Hypernian tokens`.

```curl
curl https://api.stripe.com/v1/products \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d name="Hypernian tokens"
```

```cli
stripe products create  \
  --name="Hypernian tokens"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

product = client.v1.products.create({name: 'Hypernian tokens'})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
product = client.v1.products.create({"name": "Hypernian tokens"})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$product = $stripe->products->create(['name' => 'Hypernian tokens']);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

ProductCreateParams params =
  ProductCreateParams.builder().setName("Hypernian tokens").build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Product product = client.v1().products().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const product = await stripe.products.create({
  name: 'Hypernian tokens',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.ProductCreateParams{Name: stripe.String("Hypernian tokens")}
result, err := sc.V1Products.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new ProductCreateOptions { Name = "Hypernian tokens" };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Products;
Product product = service.Create(options);
```

Next, add a flat rate [price](https://docs.stripe.com/api/prices.md) to the product with a licensed rate of 200 USD.

```curl
curl https://api.stripe.com/v1/prices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d product={{PRODUCT_ID}} \
  -d currency=usd \
  -d unit_amount=20000 \
  -d billing_scheme=per_unit \
  -d "recurring[usage_type]"=licensed \
  -d "recurring[interval]"=month
```

```cli
stripe prices create  \
  --product={{PRODUCT_ID}} \
  --currency=usd \
  --unit-amount=20000 \
  --billing-scheme=per_unit \
  -d "recurring[usage_type]"=licensed \
  -d "recurring[interval]"=month
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

price = client.v1.prices.create({
  product: '{{PRODUCT_ID}}',
  currency: 'usd',
  unit_amount: 20000,
  billing_scheme: 'per_unit',
  recurring: {
    usage_type: 'licensed',
    interval: 'month',
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
price = client.v1.prices.create({
  "product": "{{PRODUCT_ID}}",
  "currency": "usd",
  "unit_amount": 20000,
  "billing_scheme": "per_unit",
  "recurring": {"usage_type": "licensed", "interval": "month"},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$price = $stripe->prices->create([
  'product' => '{{PRODUCT_ID}}',
  'currency' => 'usd',
  'unit_amount' => 20000,
  'billing_scheme' => 'per_unit',
  'recurring' => [
    'usage_type' => 'licensed',
    'interval' => 'month',
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PriceCreateParams params =
  PriceCreateParams.builder()
    .setProduct("{{PRODUCT_ID}}")
    .setCurrency("usd")
    .setUnitAmount(20000L)
    .setBillingScheme(PriceCreateParams.BillingScheme.PER_UNIT)
    .setRecurring(
      PriceCreateParams.Recurring.builder()
        .setUsageType(PriceCreateParams.Recurring.UsageType.LICENSED)
        .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
        .build()
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
  product: '{{PRODUCT_ID}}',
  currency: 'usd',
  unit_amount: 20000,
  billing_scheme: 'per_unit',
  recurring: {
    usage_type: 'licensed',
    interval: 'month',
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PriceCreateParams{
  Product: stripe.String("{{PRODUCT_ID}}"),
  Currency: stripe.String(stripe.CurrencyUSD),
  UnitAmount: stripe.Int64(20000),
  BillingScheme: stripe.String(stripe.PriceBillingSchemePerUnit),
  Recurring: &stripe.PriceCreateRecurringParams{
    UsageType: stripe.String(stripe.PriceRecurringUsageTypeLicensed),
    Interval: stripe.String(stripe.PriceRecurringIntervalMonth),
  },
}
result, err := sc.V1Prices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PriceCreateOptions
{
    Product = "{{PRODUCT_ID}}",
    Currency = "usd",
    UnitAmount = 20000,
    BillingScheme = "per_unit",
    Recurring = new PriceRecurringOptions { UsageType = "licensed", Interval = "month" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Prices;
Price price = service.Create(options);
```

Then, add a metered price to the product with a graduated rate with two tiers.

For the first tier, specify 0 to 100,000 units at 200 USD. For the second tier, specify 0.001 USD per unit. The first tier has a price of 0 USD because the flat rate includes the first 100,000 units.

```curl
curl https://api.stripe.com/v1/prices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d product={{PRODUCT_ID}} \
  -d currency=usd \
  -d billing_scheme=tiered \
  -d "recurring[usage_type]"=metered \
  -d "recurring[interval]"=month \
  -d "recurring[meter]"={{METER_ID}} \
  -d tiers_mode=graduated \
  -d "tiers[0][up_to]"=100000 \
  -d "tiers[0][unit_amount_decimal]"=0 \
  -d "tiers[1][up_to]"=inf \
  -d "tiers[1][unit_amount_decimal]"="0.1"
```

```cli
stripe prices create  \
  --product={{PRODUCT_ID}} \
  --currency=usd \
  --billing-scheme=tiered \
  -d "recurring[usage_type]"=metered \
  -d "recurring[interval]"=month \
  -d "recurring[meter]"={{METER_ID}} \
  --tiers-mode=graduated \
  -d "tiers[0][up_to]"=100000 \
  -d "tiers[0][unit_amount_decimal]"=0 \
  -d "tiers[1][up_to]"=inf \
  -d "tiers[1][unit_amount_decimal]"=0.1
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

price = client.v1.prices.create({
  product: '{{PRODUCT_ID}}',
  currency: 'usd',
  billing_scheme: 'tiered',
  recurring: {
    usage_type: 'metered',
    interval: 'month',
    meter: '{{METER_ID}}',
  },
  tiers_mode: 'graduated',
  tiers: [
    {
      up_to: 100000,
      unit_amount_decimal: 0,
    },
    {
      up_to: 'inf',
      unit_amount_decimal: 0.1,
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
price = client.v1.prices.create({
  "product": "{{PRODUCT_ID}}",
  "currency": "usd",
  "billing_scheme": "tiered",
  "recurring": {"usage_type": "metered", "interval": "month", "meter": "{{METER_ID}}"},
  "tiers_mode": "graduated",
  "tiers": [
    {"up_to": 100000, "unit_amount_decimal": "0"},
    {"up_to": "inf", "unit_amount_decimal": "0.1"},
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$price = $stripe->prices->create([
  'product' => '{{PRODUCT_ID}}',
  'currency' => 'usd',
  'billing_scheme' => 'tiered',
  'recurring' => [
    'usage_type' => 'metered',
    'interval' => 'month',
    'meter' => '{{METER_ID}}',
  ],
  'tiers_mode' => 'graduated',
  'tiers' => [
    [
      'up_to' => 100000,
      'unit_amount_decimal' => '0',
    ],
    [
      'up_to' => 'inf',
      'unit_amount_decimal' => '0.1',
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PriceCreateParams params =
  PriceCreateParams.builder()
    .setProduct("{{PRODUCT_ID}}")
    .setCurrency("usd")
    .setBillingScheme(PriceCreateParams.BillingScheme.TIERED)
    .setRecurring(
      PriceCreateParams.Recurring.builder()
        .setUsageType(PriceCreateParams.Recurring.UsageType.METERED)
        .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
        .build()
    )
    .setTiersMode(PriceCreateParams.TiersMode.GRADUATED)
    .addTier(
      PriceCreateParams.Tier.builder()
        .setUpTo(100000L)
        .setUnitAmountDecimal(new BigDecimal("0"))
        .build()
    )
    .addTier(
      PriceCreateParams.Tier.builder()
        .setUpTo(PriceCreateParams.Tier.UpTo.INF)
        .setUnitAmountDecimal(new BigDecimal("0.1"))
        .build()
    )
    .putExtraParam("recurring[meter]", "{{METER_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Price price = client.v1().prices().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const price = await stripe.prices.create({
  product: '{{PRODUCT_ID}}',
  currency: 'usd',
  billing_scheme: 'tiered',
  recurring: {
    usage_type: 'metered',
    interval: 'month',
    meter: '{{METER_ID}}',
  },
  tiers_mode: 'graduated',
  tiers: [
    {
      up_to: 100000,
      unit_amount_decimal: '0',
    },
    {
      up_to: 'inf',
      unit_amount_decimal: '0.1',
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PriceCreateParams{
  Product: stripe.String("{{PRODUCT_ID}}"),
  Currency: stripe.String(stripe.CurrencyUSD),
  BillingScheme: stripe.String(stripe.PriceBillingSchemeTiered),
  Recurring: &stripe.PriceCreateRecurringParams{
    UsageType: stripe.String(stripe.PriceRecurringUsageTypeMetered),
    Interval: stripe.String(stripe.PriceRecurringIntervalMonth),
  },
  TiersMode: stripe.String(stripe.PriceTiersModeGraduated),
  Tiers: []*stripe.PriceCreateTierParams{
    &stripe.PriceCreateTierParams{
      UpTo: stripe.Int64(100000),
      UnitAmountDecimal: stripe.Float64(0),
    },
    &stripe.PriceCreateTierParams{
      UpToInf: stripe.String("inf"),
      UnitAmountDecimal: stripe.Float64(0.1),
    },
  },
}
params.AddExtra("recurring[meter]", "{{METER_ID}}")
result, err := sc.V1Prices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PriceCreateOptions
{
    Product = "{{PRODUCT_ID}}",
    Currency = "usd",
    BillingScheme = "tiered",
    Recurring = new PriceRecurringOptions { UsageType = "metered", Interval = "month" },
    TiersMode = "graduated",
    Tiers = new List<PriceTierOptions>
    {
        new PriceTierOptions { UpTo = 100000, UnitAmountDecimal = 0M },
        new PriceTierOptions { UpTo = PriceTierUpTo.Inf, UnitAmountDecimal = 0.1M },
    },
};
options.AddExtraParam("recurring[meter]", "{{METER_ID}}");
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Prices;
Price price = service.Create(options);
```

Finally, specify both price IDs when you [create a subscription](https://docs.stripe.com/billing/subscriptions/usage-based/implementation-guide.md#create-subscription).

```curl
curl https://api.stripe.com/v1/subscriptions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer={{CUSTOMER_ID}} \
  -d "items[0][price]"={{FLAT_MONTHLY_FEE_PRICE_ID}} \
  -d "items[0][quantity]"=1 \
  -d "items[1][price]"={{METERED_USAGE_PRICE_ID}}
```

```cli
stripe subscriptions create  \
  --customer={{CUSTOMER_ID}} \
  -d "items[0][price]"={{FLAT_MONTHLY_FEE_PRICE_ID}} \
  -d "items[0][quantity]"=1 \
  -d "items[1][price]"={{METERED_USAGE_PRICE_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

subscription = client.v1.subscriptions.create({
  customer: '{{CUSTOMER_ID}}',
  items: [
    {
      price: '{{FLAT_MONTHLY_FEE_PRICE_ID}}',
      quantity: 1,
    },
    {price: '{{METERED_USAGE_PRICE_ID}}'},
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
    {"price": "{{FLAT_MONTHLY_FEE_PRICE_ID}}", "quantity": 1},
    {"price": "{{METERED_USAGE_PRICE_ID}}"},
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
    [
      'price' => '{{FLAT_MONTHLY_FEE_PRICE_ID}}',
      'quantity' => 1,
    ],
    ['price' => '{{METERED_USAGE_PRICE_ID}}'],
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
      SubscriptionCreateParams.Item.builder()
        .setPrice("{{FLAT_MONTHLY_FEE_PRICE_ID}}")
        .setQuantity(1L)
        .build()
    )
    .addItem(
      SubscriptionCreateParams.Item.builder()
        .setPrice("{{METERED_USAGE_PRICE_ID}}")
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
      price: '{{FLAT_MONTHLY_FEE_PRICE_ID}}',
      quantity: 1,
    },
    {
      price: '{{METERED_USAGE_PRICE_ID}}',
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
    &stripe.SubscriptionCreateItemParams{
      Price: stripe.String("{{FLAT_MONTHLY_FEE_PRICE_ID}}"),
      Quantity: stripe.Int64(1),
    },
    &stripe.SubscriptionCreateItemParams{
      Price: stripe.String("{{METERED_USAGE_PRICE_ID}}"),
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
        new SubscriptionItemOptions
        {
            Price = "{{FLAT_MONTHLY_FEE_PRICE_ID}}",
            Quantity = 1,
        },
        new SubscriptionItemOptions { Price = "{{METERED_USAGE_PRICE_ID}}" },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Subscriptions;
Subscription subscription = service.Create(options);
```

### Pay as you go 

The pay as you go model (also called ?�in arrears billing?? lets you track usage incurred over a determined period, then charge the customer at the end of the period.

You can use any of the following pricing strategies:

- **Per unit**: Charge the same amount for each unit.
- **Per package**: Charge an amount for a package or bundle of units or usage.
- **Volume-based pricing**: Charge the subscription item at the tier that corresponds to the usage amount at the end of the period.
- **Graduated pricing**: Charge for the usage in each tier instead of applying a single price to all usage.

This model might cause customers to accumulate significant usage, and affect their payment method status at the end of the month.

### Credit burndown 

The credit burndown model lets you collect prepayment for usage-based products and services. Customers can use [billing credits](https://docs.stripe.com/billing/subscriptions/usage-based/billing-credits.md) to pay an initial amount, and then apply their billing credits as they use the product.

For example, Hypernian wants to sell a large enterprise contract to an existing self-serve customer for their new Hypernian Model. The customer commits to pay 100000 USD up front for Hypernian, so they can get 120000 USD of billing credit usage to use within 1 year.

#### Dashboard

#### Collect prepayment from a customer

1. On the [Invoices](https://dashboard.stripe.com/invoices) page, click **Create invoice**.
1. Select your customer from the **Customer** dropdown.
1. Select the correct currency from the **Currency** dropdown.
1. Under **Items**, select **Add a new line item**.
1. Under **Item details**, do the following:
   - For **Item**, enter ?�Hypernian Credits.??   - For **Price**, enter ??00,000.??   - Click **Save**.
1. Click **Send invoice**.

After your customer pays the invoice, you can grant them billing credits.

#### Grant billing credits to a customer

1. On the [Customers](https://dashboard.stripe.com/test/customers) page, select the customer name.
1. On the customer page, under **Credit grants**, click the plus (**+**) symbol.
1. On the **New credit grant** page, do the following:
   - For **Name**, enter a name for your credit grant.
   - For **Amount**, specify the amount of the credit grant. For the Hypernian example, enter ??20,000.??   - Under **Expiry date**, specify the date, if any, when the credits expire. For the Hypernian example, select **Specific date** and set a date 12 months from now.
   - Click **Create grant**.

#### API

First, create an invoice.

```curl
curl https://api.stripe.com/v1/invoices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d description="Hypernian Credits" \
  -d customer={{CUSTOMER_ID}} \
  -d collection_method=charge_automatically
```

```cli
stripe invoices create  \
  --description="Hypernian Credits" \
  --customer={{CUSTOMER_ID}} \
  --collection-method=charge_automatically
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.create({
  description: 'Hypernian Credits',
  customer: '{{CUSTOMER_ID}}',
  collection_method: 'charge_automatically',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.create({
  "description": "Hypernian Credits",
  "customer": "{{CUSTOMER_ID}}",
  "collection_method": "charge_automatically",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->create([
  'description' => 'Hypernian Credits',
  'customer' => '{{CUSTOMER_ID}}',
  'collection_method' => 'charge_automatically',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceCreateParams params =
  InvoiceCreateParams.builder()
    .setDescription("Hypernian Credits")
    .setCustomer("{{CUSTOMER_ID}}")
    .setCollectionMethod(InvoiceCreateParams.CollectionMethod.CHARGE_AUTOMATICALLY)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Invoice invoice = client.v1().invoices().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const invoice = await stripe.invoices.create({
  description: 'Hypernian Credits',
  customer: '{{CUSTOMER_ID}}',
  collection_method: 'charge_automatically',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceCreateParams{
  Description: stripe.String("Hypernian Credits"),
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  CollectionMethod: stripe.String(stripe.InvoiceCollectionMethodChargeAutomatically),
}
result, err := sc.V1Invoices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceCreateOptions
{
    Description = "Hypernian Credits",
    Customer = "{{CUSTOMER_ID}}",
    CollectionMethod = "charge_automatically",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.Create(options);
```

Next, add the billing credits to the invoice.

```curl
curl https://api.stripe.com/v1/invoiceitems \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer={{CUSTOMER_ID}} \
  -d currency=usd \
  -d unit_amount_decimal=10000000
```

```cli
stripe invoiceitems create  \
  --customer={{CUSTOMER_ID}} \
  --currency=usd \
  --unit-amount-decimal=10000000
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice_item = client.v1.invoice_items.create({
  customer: '{{CUSTOMER_ID}}',
  currency: 'usd',
  unit_amount_decimal: '10000000',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice_item = client.v1.invoice_items.create({
  "customer": "{{CUSTOMER_ID}}",
  "currency": "usd",
  "unit_amount_decimal": "10000000",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoiceItem = $stripe->invoiceItems->create([
  'customer' => '{{CUSTOMER_ID}}',
  'currency' => 'usd',
  'unit_amount_decimal' => '10000000',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceItemCreateParams params =
  InvoiceItemCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .setCurrency("usd")
    .setUnitAmountDecimal(new BigDecimal("10000000"))
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
InvoiceItem invoiceItem = client.v1().invoiceItems().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const invoiceItem = await stripe.invoiceItems.create({
  customer: '{{CUSTOMER_ID}}',
  currency: 'usd',
  unit_amount_decimal: '10000000',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceItemCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  Currency: stripe.String(stripe.CurrencyUSD),
  UnitAmountDecimal: stripe.Float64(10000000),
}
result, err := sc.V1InvoiceItems.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceItemCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    Currency = "usd",
    UnitAmountDecimal = 10000000M,
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.InvoiceItems;
InvoiceItem invoiceItem = service.Create(options);
```

Then, finalize the invoice.

```curl
curl https://api.stripe.com/v1/invoices/{{INVOICE_ID}}/finalize \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d auto_advance=true
```

```cli
stripe invoices finalize_invoice {{INVOICE_ID}} \
  --auto-advance=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.finalize_invoice('{{INVOICE_ID}}', {auto_advance: true})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.finalize_invoice(
  "{{INVOICE_ID}}",
  {"auto_advance": True},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->finalizeInvoice('{{INVOICE_ID}}', ['auto_advance' => true]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceFinalizeInvoiceParams params =
  InvoiceFinalizeInvoiceParams.builder().setAutoAdvance(true).build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Invoice invoice = client.v1().invoices().finalizeInvoice("{{INVOICE_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const invoice = await stripe.invoices.finalizeInvoice(
  '{{INVOICE_ID}}',
  {
    auto_advance: true,
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceFinalizeInvoiceParams{AutoAdvance: stripe.Bool(true)}
result, err := sc.V1Invoices.FinalizeInvoice(context.TODO(), "{{INVOICE_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceFinalizeOptions { AutoAdvance = true };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.FinalizeInvoice("{{INVOICE_ID}}", options);
```

After your customer pays the invoice, you can grant them billing credits.

```curl
curl https://api.stripe.com/v1/billing/credit_grants \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer={{CUSTOMER_ID}} \
  -d category=paid \
  -d "amount[type]"=monetary \
  -d "amount[monetary][value]"=12000000 \
  -d "amount[monetary][currency]"=usd \
  -d "applicability_config[scope][price_type]"=metered \
  -d expires_at=1759341179
```

```cli
stripe billing credit_grants create  \
  --customer={{CUSTOMER_ID}} \
  --category=paid \
  -d "amount[type]"=monetary \
  -d "amount[monetary][value]"=12000000 \
  -d "amount[monetary][currency]"=usd \
  -d "applicability_config[scope][price_type]"=metered \
  --expires-at=1759341179
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

credit_grant = client.v1.billing.credit_grants.create({
  customer: '{{CUSTOMER_ID}}',
  category: 'paid',
  amount: {
    type: 'monetary',
    monetary: {
      value: 12000000,
      currency: 'usd',
    },
  },
  applicability_config: {scope: {price_type: 'metered'}},
  expires_at: 1759341179,
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
credit_grant = client.v1.billing.credit_grants.create({
  "customer": "{{CUSTOMER_ID}}",
  "category": "paid",
  "amount": {"type": "monetary", "monetary": {"value": 12000000, "currency": "usd"}},
  "applicability_config": {"scope": {"price_type": "metered"}},
  "expires_at": 1759341179,
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$creditGrant = $stripe->billing->creditGrants->create([
  'customer' => '{{CUSTOMER_ID}}',
  'category' => 'paid',
  'amount' => [
    'type' => 'monetary',
    'monetary' => [
      'value' => 12000000,
      'currency' => 'usd',
    ],
  ],
  'applicability_config' => ['scope' => ['price_type' => 'metered']],
  'expires_at' => 1759341179,
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CreditGrantCreateParams params =
  CreditGrantCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .setCategory(CreditGrantCreateParams.Category.PAID)
    .setAmount(
      CreditGrantCreateParams.Amount.builder()
        .setType(CreditGrantCreateParams.Amount.Type.MONETARY)
        .setMonetary(
          CreditGrantCreateParams.Amount.Monetary.builder()
            .setValue(12000000L)
            .setCurrency("usd")
            .build()
        )
        .build()
    )
    .setApplicabilityConfig(
      CreditGrantCreateParams.ApplicabilityConfig.builder()
        .setScope(
          CreditGrantCreateParams.ApplicabilityConfig.Scope.builder()
            .setPriceType(
              CreditGrantCreateParams.ApplicabilityConfig.Scope.PriceType.METERED
            )
            .build()
        )
        .build()
    )
    .setExpiresAt(1759341179L)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
CreditGrant creditGrant = client.v1().billing().creditGrants().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const creditGrant = await stripe.billing.creditGrants.create({
  customer: '{{CUSTOMER_ID}}',
  category: 'paid',
  amount: {
    type: 'monetary',
    monetary: {
      value: 12000000,
      currency: 'usd',
    },
  },
  applicability_config: {
    scope: {
      price_type: 'metered',
    },
  },
  expires_at: 1759341179,
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.BillingCreditGrantCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  Category: stripe.String(stripe.BillingCreditGrantCategoryPaid),
  Amount: &stripe.BillingCreditGrantCreateAmountParams{
    Type: stripe.String("monetary"),
    Monetary: &stripe.BillingCreditGrantCreateAmountMonetaryParams{
      Value: stripe.Int64(12000000),
      Currency: stripe.String(stripe.CurrencyUSD),
    },
  },
  ApplicabilityConfig: &stripe.BillingCreditGrantCreateApplicabilityConfigParams{
    Scope: &stripe.BillingCreditGrantCreateApplicabilityConfigScopeParams{
      PriceType: stripe.String("metered"),
    },
  },
  ExpiresAt: stripe.Int64(1759341179),
}
result, err := sc.V1BillingCreditGrants.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Billing.CreditGrantCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    Category = "paid",
    Amount = new Stripe.Billing.CreditGrantAmountOptions
    {
        Type = "monetary",
        Monetary = new Stripe.Billing.CreditGrantAmountMonetaryOptions
        {
            Value = 12000000,
            Currency = "usd",
        },
    },
    ApplicabilityConfig = new Stripe.Billing.CreditGrantApplicabilityConfigOptions
    {
        Scope = new Stripe.Billing.CreditGrantApplicabilityConfigScopeOptions
        {
            PriceType = "metered",
        },
    },
    ExpiresAt = DateTimeOffset.FromUnixTimeSeconds(1759341179).UtcDateTime,
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Billing.CreditGrants;
Stripe.Billing.CreditGrant creditGrant = service.Create(options);
```

## See also

- [Prebuilt subscriptions page with Stripe Checkout](https://docs.stripe.com/billing/quickstart.md)
- [Build a subscriptions integration](https://docs.stripe.com/billing/subscriptions/build-subscriptions.md)
- [Embed a pricing table](https://docs.stripe.com/payments/checkout/pricing-table.md)
