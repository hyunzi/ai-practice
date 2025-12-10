# Automatically collect tax on Payment Links

Learn how to calculate and collect tax on a payment page without writing any code.

You can use Stripe Tax with [Payment Links](https://stripe.com/payments/payment-links) to automatically calculate and collect tax on a payment page and share a link to it with your customers, without writing any code.
[Watch on YouTube](https://www.youtube.com/watch?v=aotUFvYtmys)
#### Dashboard

> [Log in](https://dashboard.stripe.com/settings/tax) or [sign up](https://dashboard.stripe.com/register) for Stripe to enable Stripe Tax.

To [create a payment link](https://docs.stripe.com/payment-links/create.md) in the Dashboard:

1. Open the [Payment Links](https://dashboard.stripe.com/payment-links/create) page.
1. Click **+ New**.
1. Fill out the details.
1. Enable **Collect tax automatically**.

To update an existing payment link in the Dashboard:

1. Open the [Payment Links](https://dashboard.stripe.com/payment-links) page.
1. Select the payment link you want to update.
1. On the payment link details page, click the overflow menu (⋯), then click **Edit**.
1. In the payment link editor, select **Collect tax automatically** to enable automatic tax collection on this payment link.
1. (Optional) Select **Collect customers’ addresses** to improve tax calculation accuracy. The more information you provide, the more precise the calculation.
1. Click **Update link** to save your changes.

#### API

To create a payment link with automatic tax collection, pass the `automatic_tax[enabled]` parameter to the [Payment Link API](https://docs.stripe.com/api/payment-link/create.md) endpoint:

```curl
curl https://api.stripe.com/v1/payment_links \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "automatic_tax[enabled]"=true \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1
```

```cli
stripe payment_links create  \
  -d "automatic_tax[enabled]"=true \
  -d "line_items[0][price]"="{{PRICE_ID}}" \
  -d "line_items[0][quantity]"=1
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_link = client.v1.payment_links.create({
  automatic_tax: {enabled: true},
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_link = client.v1.payment_links.create({
  "automatic_tax": {"enabled": True},
  "line_items": [{"price": "{{PRICE_ID}}", "quantity": 1}],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentLink = $stripe->paymentLinks->create([
  'automatic_tax' => ['enabled' => true],
  'line_items' => [
    [
      'price' => '{{PRICE_ID}}',
      'quantity' => 1,
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PaymentLinkCreateParams params =
  PaymentLinkCreateParams.builder()
    .setAutomaticTax(
      PaymentLinkCreateParams.AutomaticTax.builder().setEnabled(true).build()
    )
    .addLineItem(
      PaymentLinkCreateParams.LineItem.builder()
        .setPrice("{{PRICE_ID}}")
        .setQuantity(1L)
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
  automatic_tax: {
    enabled: true,
  },
  line_items: [
    {
      price: '{{PRICE_ID}}',
      quantity: 1,
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PaymentLinkCreateParams{
  AutomaticTax: &stripe.PaymentLinkCreateAutomaticTaxParams{Enabled: stripe.Bool(true)},
  LineItems: []*stripe.PaymentLinkCreateLineItemParams{
    &stripe.PaymentLinkCreateLineItemParams{
      Price: stripe.String("{{PRICE_ID}}"),
      Quantity: stripe.Int64(1),
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
    AutomaticTax = new PaymentLinkAutomaticTaxOptions { Enabled = true },
    LineItems = new List<PaymentLinkLineItemOptions>
    {
        new PaymentLinkLineItemOptions { Price = "{{PRICE_ID}}", Quantity = 1 },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentLinks;
PaymentLink paymentLink = service.Create(options);
```

To update an existing payment link in the API, pass the `automatic_tax[enabled]` parameter to the [Payment Link API](https://docs.stripe.com/api/payment-link/update.md) endpoint:

```curl
curl https://api.stripe.com/v1/payment_links/{{PAYMENTLINK_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "automatic_tax[enabled]"=true
```

```cli
stripe payment_links update {{PAYMENTLINK_ID}} \
  -d "automatic_tax[enabled]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payment_link = client.v1.payment_links.update(
  '{{PAYMENTLINK_ID}}',
  {automatic_tax: {enabled: true}},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payment_link = client.v1.payment_links.update(
  "{{PAYMENTLINK_ID}}",
  {"automatic_tax": {"enabled": True}},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$paymentLink = $stripe->paymentLinks->update(
  '{{PAYMENTLINK_ID}}',
  ['automatic_tax' => ['enabled' => true]]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PaymentLinkUpdateParams params =
  PaymentLinkUpdateParams.builder()
    .setAutomaticTax(
      PaymentLinkUpdateParams.AutomaticTax.builder().setEnabled(true).build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
PaymentLink paymentLink =
  client.v1().paymentLinks().update("{{PAYMENTLINK_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const paymentLink = await stripe.paymentLinks.update(
  '{{PAYMENTLINK_ID}}',
  {
    automatic_tax: {
      enabled: true,
    },
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PaymentLinkUpdateParams{
  AutomaticTax: &stripe.PaymentLinkUpdateAutomaticTaxParams{Enabled: stripe.Bool(true)},
}
result, err := sc.V1PaymentLinks.Update(
  context.TODO(), "{{PAYMENTLINK_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PaymentLinkUpdateOptions
{
    AutomaticTax = new PaymentLinkAutomaticTaxOptions { Enabled = true },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.PaymentLinks;
PaymentLink paymentLink = service.Update("{{PAYMENTLINK_ID}}", options);
```

## Optional: Update your products and prices

Stripe Tax uses information stored on *products* (Products represent what your business sells—whether that's a good or a service) and *prices* (Prices define how much and how often to charge for products. This includes how much the product costs, what currency to use, and the interval if the price is for subscriptions) to calculate tax, such as *tax code* (A tax code is the category of your product for tax purposes) and *tax behavior* (Tax behavior determines whether you want to include taxes in the price ("inclusive") or add them on top ("exclusive")). If you don’t explicitly specify these configurations, Stripe Tax will use the default tax code selected in [Tax Settings](https://dashboard.stripe.com/settings/tax).

For more information, see [Specify product tax codes and tax behavior](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md).

## See also

- [Use Stripe Tax with Connect](https://docs.stripe.com/tax/connect.md)
