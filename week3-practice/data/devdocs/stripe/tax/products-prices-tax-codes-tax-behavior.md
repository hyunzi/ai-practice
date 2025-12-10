# Specify product tax codes and tax behavior

Add tax codes and tax behavior to your products and prices to automatically calculate tax.

> [Log in](https://dashboard.stripe.com/settings/tax) or [sign up](https://dashboard.stripe.com/register) for Stripe to enable Stripe Tax.

Stripe Tax uses product tax codes to associate products with their applicable tax rates. Assign each of your [products a tax code](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md#tax-code-on-product) to automatically apply the rate and other taxability rules.

Tax codes within Stripe are always the same across different jurisdictions. However, individual products might have tax treatments that differ by location, and Stripe maintains current rate and taxability information for you, while staying up to date with any changing regulations.

See our [list of available tax codes](https://docs.stripe.com/tax/tax-codes.md).

## Preset tax codes 

When activating Stripe Tax you can set two types of preset tax codes: one for products and one for shipping. You can set both in the [Tax settings](https://dashboard.stripe.com/settings/tax) in the Dashboard.
![The tax settings showing the preset tax codes, and the default shipping tax code.](https://b.stripecdn.com/docs-statics-srv/assets/pp-settings-v3.4a3660016d805248b9fb49f1bffffd76.png)

The tax settings showing the preset tax codes, and the default shipping tax code.

### Preset product tax code 

We use the preset if you don’t explicitly specify a `tax_code` on your *products* (Products represent what your business sells—whether that's a good or a service) or in [product_data](https://docs.stripe.com/api/prices/create.md#create_price-product_data) on your transactions. As you process payments, we also use the preset tax code to display the tax thresholds you might be approaching or have exceeded, under the **Threshold monitoring** section in your tax settings.

### Preset shipping tax code 

The preset shipping tax code  represents the tax treatment for shipping fees when charged. We use this if you don’t explicitly specify a `tax_code` on a shipping rate. Stripe Tax allows you to change the default shipping treatment to Nontaxable if you don’t want to charge any tax on shipping fees. We recommend you leave the default as “Shipping” to ensure the correct tax is always charged.

Tax rules for shipping fees typically follow one of two methods, depending on the state or country:

- **Proportional Allocation Method:** Shipping fees are taxed at the same rate as the items being shipped. If an order contains items with different tax rates or a mix of taxable and non-taxable goods, the shipping cost is divided proportionally based on the value of the goods. The tax rate of each item is then applied to its corresponding share of the shipping fee.
- **Highest Tax Rate Method:** The entire shipping fee is taxed at the highest rate applied to any item in the order. For example, if a customer buys a tax-exempt item and a fully taxable item, the shipping cost is taxed at the higher rate.

To charge tax on shipping for subscriptions, you can create a Product or pass `product_data` for a line item called “shipping” and select the shipping `tax_code`.

## Tax behavior 

You must specify a `tax_behavior` on a price, or a default tax behavior in the tax settings in the Dashboard, which determines how tax is presented to the buyer. This allows you to localize your checkout depending on the market.

Tax-exclusive prices are common in the US and Canada and for B2B sales in other countries. Set the tax behavior to exclusive to add tax to the subtotal amount specified in your price.

Tax-inclusive prices are common for B2C sales in many markets outside the US. When set to inclusive, the amount your buyer pays remains constant, regardless of the tax amount (zero or positive). This applies to sales subject to reverse charge as well. The unit price differs between transactions subject to reverse charge and those that aren’t. If no tax applies, the tax-inclusive price is the unit price. If the tax amount is positive, the unit price is lower, excluding the tax amount.

### Setting a default tax behavior (recommended)

You can define a default tax behavior that applies to every price that has no tax behavior defined. You can setup the default tax behavior in the [Stripe Tax settings](https://dashboard.stripe.com/settings/tax) under the **Include tax in prices** section.

After you set the default tax behavior, all prices that don’t have a `tax_behavior` defined, use this setting and are ready for Stripe Tax. The options for the default tax behavior are:

- **Automatic**: The tax behavior is based on the currency that’s chosen for a product price. For the currencies `USD` and `CAD` the tax behavior is exclusive. For all other currencies the tax behavior is inclusive. This also works with *multi-currency Prices* (A single Price object can support multiple currencies. Each purchase uses one of the supported currencies for the Price, depending on how you use the Price in your integration).
- **Inclusive**: *Inclusive tax* (Inclusive tax is tax that doesn't change the final purchase price—the price the buyer sees already includes it. Examples of inclusive tax include VAT and GST outside of the US) is already included in the price. For example, a product has the price defined as 5.00 USD. The final price the customer pays is 5.00 USD.
- **Exclusive**: *Exclusive tax* (Exclusive tax is tax that changes the final purchase price—the listed price the buyer sees doesn't include it, and it's added to the total. An example of exclusive tax is US sales tax) is added on top of the price. For example, a product has the price defined as 5.00 USD. The tax charged on this product could be 10% and would result in a final price of 5.50 USD. (Tax rates might differ—this is only an explanatory example.)

To override this setting for an individual price, [set a tax behavior on a price](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md#setting-tax-behavior-on-a-price-\(optional\)).

## Setting tax behavior on a price (optional)

You can set the tax behavior for a *Price* (Prices define how much and how often to charge for products. This includes how much the product costs, what currency to use, and the interval if the price is for subscriptions) when creating it with the Dashboard or the API. When creating a Price in the Dashboard, you can inspect the impact of your pricing model on your revenue.

> You can’t change `tax_behavior` after it’s been set to one of “exclusive” or “inclusive”.
![Tax behavior for a Price object in the Stripe Dashboard](https://b.stripecdn.com/docs-statics-srv/assets/pp_pricing.c4124697874540947a451121f0c73c4d.png)

To create a Price with `tax_behavior` through the API, it might look like this:

#### curl

```bash
curl https://api.stripe.com/v1/prices \
 -u <<YOUR_SECRET_KEY>>: \
 -d unit_amount=10000 \
 -d currency=usd \
 -d product=prod_q23fxaHasd \-d tax_behavior=exclusive \
 -d "recurring[interval]"=month
```

#### Ruby

```ruby

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

price = Stripe::Price.create(
    {
      unit_amount: 100_00,
      product: 'prod_q23fxaHasd',
      currency: 'usd',tax_behavior: 'exclusive',
      recurring: {
        interval: 'month'
      }
    }
  )
```

#### Python

```python

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

price = stripe.Price.create(
  unit_amount=10000,
  product="prod_q23fxaHasd",
  currency="usd",tax_behavior="exclusive",
  recurring={
    "interval": "month"
  },
)
```

#### PHP

```php

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
\Stripe\Stripe::setApiKey('<<YOUR_SECRET_KEY>>');

$price = \Stripe\Price::create([
  'unit_amount' => 10000,
  'product' => 'prod_q23fxaHasd',
  'currency' => 'usd','tax_behavior' => 'exclusive',
  'recurring' => [
    'interval' => 'month'
  ]
]);
```

#### Node.js

```javascript

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const price = await stripe.prices.create({
  unit_amount: 10000,
  product: "prod_q23fxaHasd",
  currency: "usd",tax_behavior: "exclusive",
  recurring: { interval: "month" }
});
```

#### Java

```java

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

PriceCreateParams params =
  PriceCreateParams.builder()
    .setUnitAmount(10000L)
    .setProduct("prod_q23fxaHasd")
    .setCurrency("usd")
    .setTaxBehavior(PriceCreateParams.TaxBehavior.EXCLUSIVE)
    .setRecurring(
      PriceCreateParams.Recurring.builder()
        .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
        .build())
    .build();

Price price = Price.create(params);

```

#### .NET

```dotnet

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

var options = new PriceCreateOptions {
  UnitAmount = 10000,
  Product = "prod_q23fxaHasd",
  Currency = "usd",
  TaxBehavior = "exclusive",
  Recurring = new PriceRecurringOptions {
    Interval = "month"
  }
};

var service = new PriceService();
Price price = service.Create(options);
```

#### Go

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

params := &stripe.PriceParams{
        UnitAmount:  stripe.Int64(10000),
        Product:     stripe.String("prod_q23fxaHasd"),
        Currency:    stripe.String("usd"),
        TaxBehavior: stripe.String("exclusive"),
        Recurring: &stripe.PriceRecurringParams{
                Interval: stripe.String("month")
        }
}

p, _ := price.New(params)
```

For a *multi-currency Price* (A single Price object can support multiple currencies. Each purchase uses one of the supported currencies for the Price, depending on how you use the Price in your integration), use the [currency_options.<currency>.tax_behavior](https://docs.stripe.com/api/prices/create.md#create_price-currency_options-tax_behavior) parameter to set different tax behaviors for different currencies.

In some cases, you might want to use a custom price that hasn’t been pre-configured. You can pass in `price_data` instead of a price ID. For example, accepting a one time payment for a custom price might look like this:

#### curl

```bash
curl https://api.stripe.com/v1/checkout/sessions \
 -u <<YOUR_SECRET_KEY>>: \
 -d success_url="https://example.com/success" \
 -d "payment_method_types[0]"=card \-d "line_items[0][price_data][currency]"="usd" \
 -d "line_items[0][price_data][unit_amount]"=10000 \
 -d "line_items[0][price_data][tax_behavior]"="exclusive" \
 -d "line_items[0][price_data][product]"="prod_Jb3wOhvaIOZZTM" \
 -d "line_items[0][quantity]"=2 \
 -d mode=payment
```

#### Ruby

```ruby

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

session = Stripe::Checkout::Session.create(
    {
      success_url: 'https://example.com/success',
      mode: 'payment',
      payment_method_types: ['card'],
      line_items: [
        {
          quantity: 2,
          price_data: {
            currency: 'usd',
            unit_amount: 100_00,
            tax_behavior: 'exclusive',
            product: 'prod_q23fxaHasd'
          }
        }
      ]
    }
  )
```

#### Python

```python

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

session = stripe.checkout.Session.create(
  success_url="https://example.com/success",
  mode="payment",
  payment_method_types=["card"],
  line_items=[
    {
      "quantity": 2,
      "price_data": {
        "currency": "usd",
        "unit_amount": 10000,
        "tax_behavior": "exclusive",
        "product": "prod_q23fxaHasd"
      }
    }
  ],
)
```

#### PHP

```php

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
\Stripe\Stripe::setApiKey('<<YOUR_SECRET_KEY>>');

$session = \Stripe\Checkout\Session::create([
  'success_url' => 'https://example.com/success',
  'mode' => 'payment',
  'payment_method_types' => ['card'],
  'line_items' => [
    [
      'quantity' => 2,
      'price_data' => [
        'currency' => 'usd',
        'unit_amount' => 10000,
        'tax_behavior' => 'exclusive',
        'product' => 'prod_q23fxaHasd'
      ]
    ]
  ]
]);
```

#### Node.js

```javascript

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  success_url: "https://example.com/success",
  mode: "payment",
  payment_method_types: ["card"],
  line_items: [
    {
      quantity: 2,
      price_data: {
        currency: "usd",
        unit_amount: 10000,
        tax_behavior: "exclusive",
        product: "prod_q23fxaHasd"
      }
    }
  ]
});
```

#### Java

```java

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

SessionCreateParams params =
  SessionCreateParams.builder()
    .setSuccessUrl("https://example.com/success")
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
    .addLineItem(
      SessionCreateParams.LineItem.builder()
        .setQuantity(2L)
        .setPriceData(
          SessionCreateParams.LineItem.PriceData.builder()
            .setCurrency("usd")
            .setUnitAmount(10000L)
            .setTaxBehavior(
              SessionCreateParams.LineItem.PriceData.TaxBehavior.EXCLUSIVE)
            .setProduct("prod_q23fxaHasd")
            .build())
        .build())
    .build();

Session session = Session.create(params);

```

#### .NET

```dotnet

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

var options = new SessionCreateOptions {
  SuccessUrl = "https://example.com/success",
  Mode = "payment",
  PaymentMethodTypes = new List<string>{ "card" },
  LineItems = new List<SessionLineItemOptions>{
    new SessionLineItemOptions {
      Quantity = 2,
      PriceData = new SessionLineItemPriceDataOptions {
        Currency = "usd",
        UnitAmount = 10000,
        TaxBehavior = "exclusive",
        Product = "prod_q23fxaHasd"
      }
    }
  }
};

var service = new SessionService();
Session session = service.Create(options);
```

#### Go

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

params := &stripe.CheckoutSessionParams{
        SuccessURL:         stripe.String("https://example.com/success"),
        Mode:               stripe.String("payment"),
        PaymentMethodTypes: stripe.StringSlice([]string{"card"}),
        LineItems: []*stripe.CheckoutSessionLineItemParams{{
                Quantity: stripe.Int64(2),
                PriceData: &stripe.CheckoutSessionLineItemPriceDataParams{
                        Currency:    stripe.String("usd"),
                        UnitAmount:  stripe.Int64(10000),
                        TaxBehavior: stripe.String("exclusive"),
                        Product:     stripe.String("prod_q23fxaHasd")
                }
        }}
}

s, _ := session.New(params)
```

## Setting a tax code on a product (recommended) 

When creating Products in the Dashboard you can set your `tax_code` in the dropdown by searching for any available [tax code](https://docs.stripe.com/tax/tax-codes.md). If you don’t, Stripe Tax uses the preset tax code defined on the [Dashboard](https://dashboard.stripe.com/settings/tax). If a product could fit multiple codes, for example, a SaaS product used for personal or business use depending on the type of customer, we recommend creating two separate products in Stripe and assigning the appropriate code to each.
![Tax codes for a product in the Stripe Dashboard](https://b.stripecdn.com/docs-statics-srv/assets/pp_product_tax_category.e6ad090b235a41108b8843420db18330.png)

To create a Product with `tax_code` using the API, it might look like this:

#### curl

```bash
curl https://api.stripe.com/v1/products \
 -u <<YOUR_SECRET_KEY>>: \
 -d name="Test Product" \
 -d tax_code="txcd_10000000"
```

#### Ruby

```ruby

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

product = Stripe::Product.create(
    { name: 'Test Product', tax_code: 'txcd_10000000' }
  )
```

#### Python

```python

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

product = stripe.Product.create(
  name="Test Product",
  tax_code="txcd_10000000",
)
```

#### PHP

```php

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
\Stripe\Stripe::setApiKey('<<YOUR_SECRET_KEY>>');

$product = \Stripe\Product::create([
  'name' => 'Test Product',
  'tax_code' => 'txcd_10000000'
]);
```

#### Node.js

```javascript

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const product = await stripe.products.create({
  name: "Test Product",
  tax_code: "txcd_10000000"
});
```

#### Java

```java

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

ProductCreateParams params =
  ProductCreateParams.builder()
    .setName("Test Product")
    .setTaxCode(""txcd_10000000"")
    .build();

Product product = Product.create(params);

```

#### .NET

```dotnet

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

var options = new ProductCreateOptions {
  Name = "Test Product",
  TaxCode = ""txcd_10000000""
};

var service = new ProductService();
Product product = service.Create(options);
```

#### Go

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

params := &stripe.ProductParams{
        Name:    stripe.String("Test Product"),
        TaxCode: stripe.String(""txcd_10000000"")
}

p, _ := product.New(params)
```

In some cases, you might want to use a custom product that hasn’t been pre-configured. You can pass in `product_data` instead of a product ID. For example, accepting a one time payment for a custom product might look like this:

#### curl

```bash
curl https://api.stripe.com/v1/checkout/sessions \
 -u <<YOUR_SECRET_KEY>>: \
 -d success_url="https://example.com/success" \
 -d "payment_method_types[0]"=card \
 -d "line_items[0][price_data][currency]"="usd" \
 -d "line_items[0][price_data][unit_amount]"=10000 \
 -d "line_items[0][price_data][tax_behavior]"="exclusive" \
 -d "line_items[0][price_data][product_data][name]"="Product name" \
 -d "line_items[0][price_data][product_data][tax_code]"="txcd_10000000" \
 -d "line_items[0][quantity]"=2 \
 -d mode=payment
```

#### Ruby

```ruby

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

session = Stripe::Checkout::Session.create(
    {
      success_url: 'https://example.com/success',
      mode: 'payment',
      payment_method_types: ['card'],
      line_items: [
        {
          quantity: 2,
          price_data: {
            currency: 'usd',
            unit_amount: 100_00,
            tax_behavior: 'exclusive',
            product_data: {
              name: 'Product name',
              tax_code: 'txcd_10000000'
            }
          }
        }
      ]
    }
  )
```

#### Python

```python

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

session = stripe.checkout.Session.create(
  success_url="https://example.com/success",
  mode="payment",
  payment_method_types=["card"],
  line_items=[
    {
      "quantity": 2,
      "price_data": {
        "currency": "usd",
        "unit_amount": 10000,
        "tax_behavior": "exclusive",
        "product_data": {
          "name": "Product name",
          "tax_code": "txcd_10000000"
        }
      }
    }
  ],
)
```

#### PHP

```php

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
\Stripe\Stripe::setApiKey('<<YOUR_SECRET_KEY>>');

$session = \Stripe\Checkout\Session::create([
  'success_url' => 'https://example.com/success',
  'mode' => 'payment',
  'payment_method_types' => ['card'],
  'line_items' => [
    [
      'quantity' => 2,
      'price_data' => [
        'currency' => 'usd',
        'unit_amount' => 10000,
        'tax_behavior' => 'exclusive',
        'product_data' => [
          'name' => 'Product name',
          'tax_code' => 'txcd_10000000'
        ]
      ]
    ]
  ]
]);
```

#### Node.js

```javascript

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const session = await stripe.checkout.sessions.create({
  success_url: "https://example.com/success",
  mode: "payment",
  payment_method_types: ["card"],
  line_items: [
    {
      quantity: 2,
      price_data: {
        currency: "usd",
        unit_amount: 10000,
        tax_behavior: "exclusive",
        product_data: { name: "Product name", tax_code: "txcd_10000000" }
      }
    }
  ]
});
```

#### Java

```java

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

SessionCreateParams params =
  SessionCreateParams.builder()
    .setSuccessUrl("https://example.com/success")
    .setMode(SessionCreateParams.Mode.PAYMENT)
    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
    .addLineItem(
        SessionCreateParams.LineItem.builder()
          .setQuantity(2L)
          .setPriceData(
            SessionCreateParams.LineItem.PriceData.builder()
              .setCurrency("usd")
              .setUnitAmount(10000L)
              .setTaxBehavior(
                SessionCreateParams.LineItem.PriceData.TaxBehavior.EXCLUSIVE)
              .setProductData(
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                  .setName("Product name")
                  .setTaxCode(""txcd_10000000"")
                  .build())
                .build())
            .build())
      .build();

Session session = Session.create(params);

```

#### .NET

```dotnet

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

var options = new SessionCreateOptions {
  SuccessUrl = "https://example.com/success",
  Mode = "payment",
  PaymentMethodTypes = new List<string>{ "card" },
  LineItems = new List<SessionLineItemOptions>{
    new SessionLineItemOptions {
      Quantity = 2,
      PriceData = new SessionLineItemPriceDataOptions {
        Currency = "usd",
        UnitAmount = 10000,
        TaxBehavior = "exclusive",
        ProductData = new SessionLineItemPriceDataProductDataOptions {
          Name = "Product name",
          TaxCode = ""txcd_10000000""
        }
      }
    }
  }
};

var service = new SessionService();
Session session = service.Create(options);
```

#### Go

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

params := &stripe.CheckoutSessionParams{
        SuccessURL:         stripe.String("https://example.com/success"),
        Mode:               stripe.String("payment"),
        PaymentMethodTypes: stripe.StringSlice([]string{"card"}),
        LineItems: []*stripe.CheckoutSessionLineItemParams{&stripe.CheckoutSessionLineItemParams{
                Quantity: stripe.Int64(2),
                PriceData: &stripe.CheckoutSessionLineItemPriceDataParams{
                        Currency:    stripe.String("usd"),
                        UnitAmount:  stripe.Int64(10000),
                        TaxBehavior: stripe.String("exclusive"),
                        ProductData: &stripe.CheckoutSessionLineItemPriceDataProductDataParams{
                                Name:    stripe.String("Product name"),
                                TaxCode: stripe.String(""txcd_10000000"")
                        }
                }
        }}
}

s, _ := session.New(params)
```

## Creating a shipping rate with tax code (optional) 

Checkout payment mode allows you to set shipping rates and charge tax on shipping. You can automatically calculate tax on shipping charges by setting the tax code on the shipping rate in the Dashboard or [API](https://docs.stripe.com/api/shipping_rates.md).
![Shipping rate with a tax code in the Stripe Dashboard](https://b.stripecdn.com/docs-statics-srv/assets/pp_shipping_rate_v3.a204f73ab02310683aace14717d960f4.png)

#### curl

```bash
curl https://api.stripe.com/v1/shipping_rates \
 -u <<YOUR_SECRET_KEY>>: \
 -d display_name="Ground shipping" \
 -d type="fixed_amount" \
 -d "fixed_amount[amount]"=500 \
 -d "fixed_amount[currency]"=usd \
 -d tax_behavior="inclusive" \
 -d tax_code="txcd_92010001"
```

#### Ruby

```ruby

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

shipping_rate = Stripe::ShippingRate.create(
    {
      display_name: 'Ground shipping',
      type: 'fixed_amount',
      fixed_amount: {
        amount: 500,
        currency: 'usd'
      },
      tax_behavior: 'inclusive',
      tax_code: 'txcd_92010001'
    }
  )
```

#### Python

```python

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

shipping_rate = stripe.ShippingRate.create(
  display_name="Ground shipping",
  type="fixed_amount",
  fixed_amount={
    'amount': 500,
    'currency': 'usd'
  },
  tax_behavior="inclusive",
  tax_code="txcd_92010001",
)
```

#### PHP

```php

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
\Stripe\Stripe::setApiKey('<<YOUR_SECRET_KEY>>');

$shipping_rate = \Stripe\ShippingRate::create([
  'display_name' => 'Ground shipping',
  'type' => 'fixed_amount',
  'fixed_amount' => [
    'amount' => 500,
    'currency' => 'usd'
  ],
  'tax_behavior' => 'inclusive',
  'tax_code' => 'txcd_92010001'
]);
```

#### Node.js

```javascript

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const shippingRate = await stripe.shippingRates.create({
  name: "Ground shipping",
  type: 'fixed_amount',
  fixed_amount: {
    amount: 500,
    currency: 'usd'
  },
  tax_behavior: "inclusive",
  tax_code: "txcd_92010001"
});
```

#### Java

```java

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

ShippingRateCreateParams params =
  ShippingRateCreateParams.builder()
    .setDisplayName("Ground shipping")
    .setType(ShippingRateCreateParams.Type.FIXED_AMOUNT)
    .setFixedAmount(
      ShippingRateCreateParams.FixedAmount.builder()
        .setAmount(500)
        .setCurrency("usd")
        .build())
    .setTaxBehavior(ShippingRateCreateParams.TaxBehavior.INCLUSIVE)
    .setTaxCode("txcd_92010001")
    .build();

ShippingRate shippingRate = ShippingRate.create(params);

```

#### .NET

```dotnet

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

var options = new ShippingRateCreateOptions {
  DisplayName = "Ground shipping",
  Type = "fixed_amount",
  FixedAmount = new ShippingRateFixedAmountOptions
  {
    Amount = 500,
    Currency = "usd"
  },
  TaxBehavior = "exclusive",
  TaxCode = "txcd_92010001"
};

var service = new ShippingRateService();
ShippingRate shippingRate = service.Create(options);
```

#### Go

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

params := &stripe.ShippingRateParams{
        DisplayName: stripe.String("Ground shipping"),
        Type: stripe.String("fixed_amount"),
        FixedAmount: &stripe.ShippingRateFixedAmountParams{
          Amount: stripe.Int64(500),
          Currency: stripe.String("usd")
        },
        TaxBehavior: stripe.String("inclusive"),
        TaxCode:     stripe.String("txcd_92010001")
}

s, _ := shippingrate.New(params)
```

## See also

- [Checkout and Tax](https://docs.stripe.com/tax/checkout.md)
- [Invoicing and Tax](https://docs.stripe.com/tax/invoicing.md)
- [Determining customer locations](https://docs.stripe.com/tax/customer-locations.md)
