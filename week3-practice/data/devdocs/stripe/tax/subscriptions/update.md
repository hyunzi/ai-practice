# Update existing subscriptions

Learn how to update existing subscriptions to Stripe Tax.

[Stripe Tax](https://docs.stripe.com/tax.md) allows you to calculate the tax to collect on your transactions. It computes the taxes and adds them to the payment automatically, based on the product and the customer location.

When you integrate with Stripe Tax, you need to update existing subscriptions to make sure that tax is automatically calculated going forward. Stripe provides tooling to help you update your subscriptions. You can also manually update subscriptions where you want more control over certain options.

## Update existing subscriptions using automated tooling

First, you need to activate Stripe Tax. To learn how, read the [setup guide](https://docs.stripe.com/tax/set-up.md).

To use the tooling, follow these steps:

1. Go to the [Dashboard](https://dashboard.stripe.com/tax/migrations).
1. Review the subscriptions you need to update.
1. Review the recommended actions.
1. Make any necessary manual updates.

Stripe removes manual [tax rates](https://docs.stripe.com/billing/taxes/tax-rates.md) from the subscriptions, which can take up to 5 business days. When the process is complete, we notify you by email.

> We don’t prorate the tax changes. The updates take effect at the start of the next billing cycle.

You can use the tooling to update subscriptions that meet the following criteria:

- Are active
- Don’t automatically collect tax
- Have sufficient [address information](https://docs.stripe.com/tax/customer-locations.md#address-hierarchy-other) to calculate tax
- Have the [tax behavior](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md#setting-tax-behavior-on-a-price-\(optional\)) set on the price

You need to update the following types of subscriptions:

- Subscriptions with schedules. To learn more, see [the Update Subscriptions with subscription schedules section](https://docs.stripe.com/tax/subscriptions/update.md#existing-subscription-schedules).
- Subscriptions that use the [charge types](https://docs.stripe.com/connect/charges.md#types) destination charges or separate charges and transfers.

The automated tool might not be able to update some subscriptions. This is usually due to not having a [valid customer address](https://docs.stripe.com/tax/customer-locations.md#other).

> The automated tool doesn’t work with test subscriptions.

After using the automated tool, you have to wait a few days before you can run it again. During that time, check and fix the subscriptions that failed in the first attempt.

## Update existing subscriptions manually

- [Review customer locations](https://docs.stripe.com/tax/subscriptions/update.md#customer-locations) and make any required updates.
- [Update products and prices](https://docs.stripe.com/tax/subscriptions/update.md#products_prices) with tax codes and tax behaviors.
- [Update subscriptions](https://docs.stripe.com/tax/subscriptions/update.md#subs) to automatically calculate taxes on future invoices.
- [Confirm](https://docs.stripe.com/tax/subscriptions/update.md#confirm) that you’ve updated the subscriptions correctly.

## Check customer locations

To correctly calculate tax, we need to know the customer’s tax location status. You can check it in the Dashboard or in exported data, or get the information using the API.

#### Dashboard

To check a customer’s tax location status through the Dashboard, go to the [Customers page](https://dashboard.stripe.com/customers), select the customer, and expand their details. The tax location status (`automatic_tax`) has four possible values:

| Status                                          | Description                                                                                             | Possible Action                                                                                                                                                                                                                                                                                             |
| ----------------------------------------------- | ------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Valid (`supported`)                             | Automatic tax fully supported.                                                                          | No further action required.                                                                                                                                                                                                                                                                                 |
| Unrecognized location (`unrecognized_location`) | The address isn’t valid for determining a tax location.                                                 | Ask the customer for an updated address and set [customer.address](https://docs.stripe.com/api/customers/update.md#update_customer-address) to the new value. You can update the value through the API or Dashboard by editing the customer’s details.                                                      |
| Not registered (`not_collecting`)               | The address is recognized and resolved to a location that you haven’t set up a collection location for. | The action to take depends on your [tax obligations](https://docs.stripe.com/tax/monitoring.md). If you proceed, Stripe Tax doesn’t assess any taxes. If you want it to assess tax, [add an active registration](https://docs.stripe.com/tax/registering.md) for the jurisdiction the customer is based in. |
| `failed`                                        | An [error](https://docs.stripe.com/error-codes.md) occurred with Stripe’s servers. This is rare.        | Try the request again or contact Stripe support for additional assistance.                                                                                                                                                                                                                                  |

In case the `status=unrecognized_location` you need to update the customer location with [an address that Stripe Tax can use](https://docs.stripe.com/tax/customer-locations.md). In the Dashboard, you can go into the [Customers page](https://dashboard.stripe.com/customers), select the customer, and change its billing or shipping address under Details.

### Check tax location status through exports

To check the tax location status of your customers through Dashboard exports, go to the [Customers page](https://dashboard.stripe.com/customers), click **Export**, and select **All columns**.

The CSV file includes a Boolean column named **Tax Location Recognized** that you can use to determine whether a customer has a [valid](https://docs.stripe.com/tax/customer-locations.md#supported-formats) address.

#### API

To check a customer’s tax location status using the API, retrieve the [Customer object](https://docs.stripe.com/api/customers/object.md) and [expand](https://docs.stripe.com/api/expanding_objects.md) the response by adding `expand: ['tax']` to your request. You must use `expand` because the default response doesn’t include the [tax](https://docs.stripe.com/api/customers/object.md#customer_object-tax) field.

```curl
curl -G https://api.stripe.com/v1/customers/{{CUSTOMER_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "expand[]"=tax
```

```cli
stripe customers retrieve {{CUSTOMER_ID}} \
  -d "expand[0]"=tax
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

customer = client.v1.customers.retrieve('{{CUSTOMER_ID}}', {expand: ['tax']})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
customer = client.v1.customers.retrieve(
  "{{CUSTOMER_ID}}",
  {"expand": ["tax"]},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$customer = $stripe->customers->retrieve('{{CUSTOMER_ID}}', ['expand' => ['tax']]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CustomerRetrieveParams params = CustomerRetrieveParams.builder().addExpand("tax").build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Customer customer = client.v1().customers().retrieve("{{CUSTOMER_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const customer = await stripe.customers.retrieve(
  '{{CUSTOMER_ID}}',
  {
    expand: ['tax'],
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CustomerRetrieveParams{}
params.AddExpand("tax")
result, err := sc.V1Customers.Retrieve(context.TODO(), "{{CUSTOMER_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CustomerGetOptions { Expand = new List<string> { "tax" } };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Customers;
Customer customer = service.Get("{{CUSTOMER_ID}}", options);
```

The response includes expanded tax fields that indicate the computed tax location, and whether you can use automatic tax calculation with the given customer.

```json
{"tax": {
    "automatic_tax": "supported",
    "ip_address": null,
    "location": {"country": "US", "state": "CA", "source": "billing_address"}
  },
  "id": "cus_JvecILHjNPGKSI",
  "object": "customer",
  "address": null,
  "balance": 0,
  "created": 1627380831,
  "currency": null,
  "default_source": "src_0JHnP4507O8KAxCGgDurDgoS",
  "delinquent": false,
  "description": "1st Deposit",
  "discount": null,
  "email": "jenny.rosen@example.com",
  "invoice_prefix": "66CD4DB9",
  "invoice_settings": {
    "custom_fields": null,
    "default_payment_method": null,
    "footer": null
  },
  "livemode": false,
  "metadata": {
  },
  "name": "Jenny Rosen",
  "next_invoice_sequence": 1,
  "phone": null,
  "preferred_locales": [
  ],
  "shipping": null,
  "tax_exempt": "none"
}
```

The value of `automatic_tax` (**Tax location status** in the Dashboard) has four possible statuses:

| Status                                          | Description                                                                                             | Possible Action                                                                                                                                                                                                                                                                                             |
| ----------------------------------------------- | ------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Valid (`supported`)                             | Automatic tax fully supported.                                                                          | No further action required.                                                                                                                                                                                                                                                                                 |
| Unrecognized location (`unrecognized_location`) | The address isn’t valid for determining a tax location.                                                 | Ask the customer for an updated address and set [customer.address](https://docs.stripe.com/api/customers/update.md#update_customer-address) to the new value. You can update the value through the API or Dashboard by editing the customer’s details.                                                      |
| Not registered (`not_collecting`)               | The address is recognized and resolved to a location that you haven’t set up a collection location for. | The action to take depends on your [tax obligations](https://docs.stripe.com/tax/monitoring.md). If you proceed, Stripe Tax doesn’t assess any taxes. If you want it to assess tax, [add an active registration](https://docs.stripe.com/tax/registering.md) for the jurisdiction the customer is based in. |
| `failed`                                        | An [error](https://docs.stripe.com/error-codes.md) occurred with Stripe’s servers. This is rare.        | Try the request again or contact Stripe support for additional assistance.                                                                                                                                                                                                                                  |

In case the `status=unrecognized_location` you need to update the customer location with [an address that Stripe Tax can use to calculate tax](https://docs.stripe.com/tax/customer-locations.md). You can [update the customer](https://docs.stripe.com/api/customers/update.md#update_customer-address) through the API:

```curl
curl https://api.stripe.com/v1/customers/{{CUSTOMER_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "address[line1]"="27 Fredrick Ave" \
  -d "address[city]"=Brothers \
  -d "address[postal_code]"=97712 \
  -d "address[state]"=OR \
  -d "address[country]"=US \
  -d "expand[]"=tax
```

```cli
stripe customers update {{CUSTOMER_ID}} \
  -d "address[line1]"="27 Fredrick Ave" \
  -d "address[city]"=Brothers \
  -d "address[postal_code]"=97712 \
  -d "address[state]"=OR \
  -d "address[country]"=US \
  -d "expand[0]"=tax
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

customer = client.v1.customers.update(
  '{{CUSTOMER_ID}}',
  {
    address: {
      line1: '27 Fredrick Ave',
      city: 'Brothers',
      postal_code: '97712',
      state: 'OR',
      country: 'US',
    },
    expand: ['tax'],
  },
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
customer = client.v1.customers.update(
  "{{CUSTOMER_ID}}",
  {
    "address": {
      "line1": "27 Fredrick Ave",
      "city": "Brothers",
      "postal_code": "97712",
      "state": "OR",
      "country": "US",
    },
    "expand": ["tax"],
  },
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$customer = $stripe->customers->update(
  '{{CUSTOMER_ID}}',
  [
    'address' => [
      'line1' => '27 Fredrick Ave',
      'city' => 'Brothers',
      'postal_code' => '97712',
      'state' => 'OR',
      'country' => 'US',
    ],
    'expand' => ['tax'],
  ]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CustomerUpdateParams params =
  CustomerUpdateParams.builder()
    .setAddress(
      CustomerUpdateParams.Address.builder()
        .setLine1("27 Fredrick Ave")
        .setCity("Brothers")
        .setPostalCode("97712")
        .setState("OR")
        .setCountry("US")
        .build()
    )
    .addExpand("tax")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Customer customer = client.v1().customers().update("{{CUSTOMER_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const customer = await stripe.customers.update(
  '{{CUSTOMER_ID}}',
  {
    address: {
      line1: '27 Fredrick Ave',
      city: 'Brothers',
      postal_code: '97712',
      state: 'OR',
      country: 'US',
    },
    expand: ['tax'],
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CustomerUpdateParams{
  Address: &stripe.AddressParams{
    Line1: stripe.String("27 Fredrick Ave"),
    City: stripe.String("Brothers"),
    PostalCode: stripe.String("97712"),
    State: stripe.String("OR"),
    Country: stripe.String("US"),
  },
}
params.AddExpand("tax")
result, err := sc.V1Customers.Update(context.TODO(), "{{CUSTOMER_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CustomerUpdateOptions
{
    Address = new AddressOptions
    {
        Line1 = "27 Fredrick Ave",
        City = "Brothers",
        PostalCode = "97712",
        State = "OR",
        Country = "US",
    },
    Expand = new List<string> { "tax" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Customers;
Customer customer = service.Update("{{CUSTOMER_ID}}", options);
```

For more information on which customer address is valid, how they’re used, or how to handle errors, see [Collect customer addresses](https://docs.stripe.com/tax/customer-locations.md).

## Update products and prices

Your products and prices use the default tax behavior you assigned when activating Stripe Tax. If you’d prefer to update active products and prices to calculate tax independently, set a tax_code and tax_behavior. See the full list of [available tax codes](https://docs.stripe.com/tax/tax-codes.md) and the [guide for setting up](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md) tax codes and tax behavior for more information.

### Update products

First, update any existing products with a `tax_code`. If you don’t explicitly define a `tax_code` on your product, Stripe Tax uses the preset product tax code from your settings.

#### Dashboard

To update a Product with a `tax_code` in the Dashboard, go to the [Products page](https://dashboard.stripe.com/products?active=true), select a product to edit and, in the product information page, choose the tax code from the drop-down menu.

#### API

Here’s how to update a product with a `tax_code` using the API:

```curl
curl https://api.stripe.com/v1/products/{{PRODUCT_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d tax_code="{{TAXCODE_ID}}"
```

```cli
stripe products update {{PRODUCT_ID}} \
  --tax-code="{{TAXCODE_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

product = client.v1.products.update(
  '{{PRODUCT_ID}}',
  {tax_code: '{{TAXCODE_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
product = client.v1.products.update(
  "{{PRODUCT_ID}}",
  {"tax_code": "{{TAXCODE_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$product = $stripe->products->update(
  '{{PRODUCT_ID}}',
  ['tax_code' => '{{TAXCODE_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

ProductUpdateParams params =
  ProductUpdateParams.builder().setTaxCode("{{TAXCODE_ID}}").build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Product product = client.v1().products().update("{{PRODUCT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const product = await stripe.products.update(
  '{{PRODUCT_ID}}',
  {
    tax_code: '{{TAXCODE_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.ProductUpdateParams{TaxCode: stripe.String("{{TAXCODE_ID}}")}
result, err := sc.V1Products.Update(context.TODO(), "{{PRODUCT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new ProductUpdateOptions { TaxCode = "{{TAXCODE_ID}}" };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Products;
Product product = service.Update("{{PRODUCT_ID}}", options);
```

### Update prices

Next, update the tax behavior for your prices.

> You can’t change `tax_behavior` after it’s been set to one of `exclusive` or `inclusive`. If you want to change the tax behavior of a price, you need to create a new price with the desired behavior, and archive the old price.

#### Dashboard

To update a price using the Dashboard:

1. Go to the [products page](https://dashboard.stripe.com/products).
1. Select the product with the price you want to update.
1. Select additional options in the price information section.
1. In the **Include tax in price** drop-down menu, select the behavior you want to associate with the price.

#### API

Here’s how to update a price using the API:

```curl
curl https://api.stripe.com/v1/prices/{{PRICE_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d tax_behavior=exclusive
```

```cli
stripe prices update {{PRICE_ID}} \
  --tax-behavior=exclusive
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

price = client.v1.prices.update('{{PRICE_ID}}', {tax_behavior: 'exclusive'})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
price = client.v1.prices.update(
  "{{PRICE_ID}}",
  {"tax_behavior": "exclusive"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$price = $stripe->prices->update('{{PRICE_ID}}', ['tax_behavior' => 'exclusive']);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PriceUpdateParams params =
  PriceUpdateParams.builder()
    .setTaxBehavior(PriceUpdateParams.TaxBehavior.EXCLUSIVE)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Price price = client.v1().prices().update("{{PRICE_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const price = await stripe.prices.update(
  '{{PRICE_ID}}',
  {
    tax_behavior: 'exclusive',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PriceUpdateParams{
  TaxBehavior: stripe.String(stripe.PriceTaxBehaviorExclusive),
}
result, err := sc.V1Prices.Update(context.TODO(), "{{PRICE_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PriceUpdateOptions { TaxBehavior = "exclusive" };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Prices;
Price price = service.Update("{{PRICE_ID}}", options);
```

## Update subscriptions

With your customers, products, and prices updated, you’re ready to update existing subscriptions.

Get the list of subscriptions that need to be updated from the [subscriptions page in the Dashboard](https://dashboard.stripe.com/subscriptions). To display only subscriptions that don’t have automatic tax enabled, click **Filter**, check **Automatic tax**, and select **Disabled**. Alternatively, you can export all filtered subscriptions to view them as a CSV file. To do this, click **Export** and select **All** as the **Date range**.

How you update the subscriptions depends on their state:

- If your subscriptions [don’t have existing tax rates](https://docs.stripe.com/tax/subscriptions/update.md#no-tax-rates), you only need to enable automatic tax.
- If your subscriptions have [existing tax rates](https://docs.stripe.com/tax/subscriptions/update.md#existing-tax-rates) (at either the subscription or line-item level), you need to clear out any existing tax rates and enable automatic tax. To avoid creating prorated items, you can schedule this update.
- If your subscriptions have [subscriptions schedules](https://docs.stripe.com/tax/subscriptions/update.md#existing-subscription-schedules), you need to remove instances of `automatic_tax[enabled]=false` in the subscription schedule plans.

### Update subscriptions with no existing tax rates 

#### Dashboard

To update subscriptions with no existing tax rates using the Dashboard, update the subscription and turn on the **Calculate tax automatically** option.

#### API

To [update subscriptions](https://docs.stripe.com/api/subscriptions/update.md) that you haven’t configured [tax rates](https://docs.stripe.com/billing/taxes/tax-rates.md) for, set [automatic_tax.enabled](https://docs.stripe.com/api/subscriptions/update.md#update_subscription-automatic_tax) to `true`.

```curl
curl https://api.stripe.com/v1/subscriptions/{{SUBSCRIPTION_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "automatic_tax[enabled]"=true
```

```cli
stripe subscriptions update {{SUBSCRIPTION_ID}} \
  -d "automatic_tax[enabled]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

subscription = client.v1.subscriptions.update(
  '{{SUBSCRIPTION_ID}}',
  {automatic_tax: {enabled: true}},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
subscription = client.v1.subscriptions.update(
  "{{SUBSCRIPTION_ID}}",
  {"automatic_tax": {"enabled": True}},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$subscription = $stripe->subscriptions->update(
  '{{SUBSCRIPTION_ID}}',
  ['automatic_tax' => ['enabled' => true]]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SubscriptionUpdateParams params =
  SubscriptionUpdateParams.builder()
    .setAutomaticTax(
      SubscriptionUpdateParams.AutomaticTax.builder().setEnabled(true).build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Subscription subscription =
  client.v1().subscriptions().update("{{SUBSCRIPTION_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const subscription = await stripe.subscriptions.update(
  '{{SUBSCRIPTION_ID}}',
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
params := &stripe.SubscriptionUpdateParams{
  AutomaticTax: &stripe.SubscriptionUpdateAutomaticTaxParams{Enabled: stripe.Bool(true)},
}
result, err := sc.V1Subscriptions.Update(
  context.TODO(), "{{SUBSCRIPTION_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new SubscriptionUpdateOptions
{
    AutomaticTax = new SubscriptionAutomaticTaxOptions { Enabled = true },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Subscriptions;
Subscription subscription = service.Update("{{SUBSCRIPTION_ID}}", options);
```

Setting `automatic_tax.enabled=true` activates automatic tax calculations for all subsequent invoices created for that subscription.

### Update subscriptions with existing tax rates 

#### Dashboard

To update subscriptions with [tax rates](https://docs.stripe.com/billing/taxes/tax-rates.md) through the Dashboard, edit the subscription, then enable the **calculate tax automatically** option. The Dashboard removes any existing tax rates and automatically calculates tax going forward. If you haven’t updated your prices to set `tax_behavior`, the Dashboard prompts you to update any missing details before you can update the subscription.

#### API

To update subscriptions with [tax rates](https://docs.stripe.com/billing/taxes/tax-rates.md) set at the [subscription level](https://docs.stripe.com/billing/taxes/collect-taxes.md?tax-calculation=tax-rates#static-configuration), you need to remove the tax rates before enabling `automatic_tax`. When you make the update:

- Pass an empty string in the [default_tax_rates](https://docs.stripe.com/api/subscriptions/update.md#update_subscription-default_tax_rates) and [tax_rates](https://docs.stripe.com/api/subscriptions/object.md#subscription_object-items-data-tax_rates) fields for each subscription [item](https://docs.stripe.com/api/subscriptions/object.md#subscription_object-items). Doing this clears out tax rates set at both the subscription (`default_tax_rates`) and line-item (`tax_rates`) levels.
- Set [automatic_tax.enabled](https://docs.stripe.com/api/subscriptions/update.md#update_subscription-automatic_tax) to `true`.
- Set [proration_behavior](https://docs.stripe.com/api/subscriptions/update.md#update_subscription-proration_behavior) to `none`.

#### Ruby

```ruby

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

subscription = Stripe::Subscription.retrieve('{{SUBSCRIPTION_ID}}')
Stripe::Subscription.update(
  subscription.id,
  {
    automatic_tax: { enabled: true },
    # Removes existing tax_rates for each item in the subscription
    items: subscription.items.data.map {|item| {id: item.id, tax_rates: ''}},
    default_tax_rates: '',
    proration_behavior: 'none'
  }
)
```

#### Python

```python

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
stripe.api_key = '<<YOUR_SECRET_KEY>>'

subscription = stripe.Subscription.retrieve('{{SUBSCRIPTION_ID}}')
stripe.Subscription.modify(
  subscription.id,
  automatic_tax={"enabled": True},
  # Removes existing tax_rates for each item in the subscription
  items=list(map(lambda item: {"id": item.id, "tax_rates": ""}, subscription['items']['data'])),
  default_tax_rates="",
  proration_behavior="none"
)
```

#### PHP

```php

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
\Stripe\Stripe::setApiKey('<<YOUR_SECRET_KEY>>');

$subscription = $stripe->subscriptions->retrieve(
  '{{SUBSCRIPTION_ID}}',
  []
);

$passEmptyTaxRates = function($item) {
  return [
    'id' => $item->id,
    'tax_rates' => ''
  ];
};

$stripe->subscriptions->update(
  $subscription->id,
  [
    'automatic_tax' => ['enabled' => true],
    // Removes existing tax_rates for each item in the subscription
    'items' => array_map($passEmptyTaxRates, $subscription->items->data),
    'default_tax_rates' => '',
    'proration_behavior' => 'none'
  ],
);
```

#### Node.js

```javascript

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const subscription = await stripe.subscriptions.retrieve('{{SUBSCRIPTION_ID}}');
const updatedSubscription = await stripe.subscriptions.update(
  subscription.id,
  {
    automatic_tax: {enabled: true},
    // Removes existing tax_rates for each item in the subscription
    items: subscription.items.data.map(item => ({id: item.id, tax_rates: ''})),
    default_tax_rates: '',
    proration_behavior: 'none',
  },
);
```

#### Java

```java

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

Subscription subscription = Subscription.retrieve("{{SUBSCRIPTION_ID}}");

Map<String, Object> automaticTax = new HashMap<>();
automaticTax.put("enabled", true);

// Building an array containing the subscription item ids
// with tax_rates="" to remove tax_rates for each item.
ArrayList<Map<String, String>> itemsWithEmptyTaxRates = new ArrayList<>();
for (SubscriptionItem item: subscription.getItems().getData()) {
  Map<String, String> newItem = new HashMap<>();
  newItem.put("id", item.getId());
  newItem.put("tax_rates", "");
  itemsWithEmptyTaxRates.add(newItem);
}

Map<String, Object> params = new HashMap<>();
params.put("automatic_tax", automaticTax);
params.put("items", items);
params.put("default_tax_rates", "");
params.put("proration_behavior", "none");

Subscription updatedSubscription = subscription.update(params);

```

#### .NET

```dotnet

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

var service = new SubscriptionService();
var subscription = service.Get("sub_JzUpaTphIGpTOI");
var options = new SubscriptionUpdateOptions
{
    AutomaticTax = new SubscriptionAutomaticTaxOptions
    {
        Enabled = true
    },
    // Removes existing tax_rates for each item in the subscription
    Items = subscription.Items.Data.ConvertAll<SubscriptionItemOptions>(
        item => new SubscriptionItemOptions{Id = item.Id, TaxRates = new List<String>()}
    ),
    DefaultTaxRates = new List<String>(),
    ProrationBehavior = "none"
};
service.Update(subscription.Id, options);
```

#### Go

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

s, _ := subscription.Get("sub_JzMkw4IV17vNnb", nil)
// Building an array containing the subscription item ids
// and empty tax_rates slices. When passed to the update
// method, this will remove tax_rates for each item.
itemsWithEmptyTaxRates := []*stripe.SubscriptionItemsParams{}
for _, item := range s.Items.Data {
  itemsWithEmptyTaxRates = append(itemsWithEmptyTaxRates, &stripe.SubscriptionItemsParams{
    ID:       stripe.String(item.ID),
    TaxRates: stripe.StringSlice(nil),
  })
}

params := &stripe.SubscriptionParams{
  AutomaticTax: &stripe.SubscriptionAutomaticTaxParams{
    Enabled: stripe.Bool(true),
  },
  Items:             itemsWithEmptyTaxRates,
  DefaultTaxRates:   stripe.StringSlice(nil),
  ProrationBehavior: stripe.String("none"),
}
subscription.Update(s.ID, params)
```

### Update Subscriptions with subscription schedules 

If you need to collect tax, and any of your subscriptions include a subscription schedule that sets `automatic_tax[enabled]=false`, you must remove that parameter. To do so, update all phases of the subscription’s schedule by removing `automatic_tax[enabled]=false` and setting `default_settings[automatic_tax][enabled]=true`.

When you update a subscription schedule, you need to pass in all current and future phases. To do this, verify the set parameters, then enable Stripe Tax in the subscription schedule.

```curl
curl https://api.stripe.com/v1/subscription_schedules/{{SUBSCRIPTIONSCHEDULE_ID}} \
  -u "<<YOUR_SECRET_KEY>>:"
```

```cli
stripe subscription_schedules retrieve {{SUBSCRIPTIONSCHEDULE_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

subscription_schedule = client.v1.subscription_schedules.retrieve('{{SUBSCRIPTIONSCHEDULE_ID}}')
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
subscription_schedule = client.v1.subscription_schedules.retrieve(
  "{{SUBSCRIPTIONSCHEDULE_ID}}",
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$subscriptionSchedule = $stripe->subscriptionSchedules->retrieve(
  '{{SUBSCRIPTIONSCHEDULE_ID}}',
  []
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SubscriptionScheduleRetrieveParams params =
  SubscriptionScheduleRetrieveParams.builder().build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
SubscriptionSchedule subscriptionSchedule =
  client.v1().subscriptionSchedules().retrieve(
    "{{SUBSCRIPTIONSCHEDULE_ID}}",
    params
  );
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const subscriptionSchedule = await stripe.subscriptionSchedules.retrieve(
  '{{SUBSCRIPTIONSCHEDULE_ID}}'
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SubscriptionScheduleRetrieveParams{}
result, err := sc.V1SubscriptionSchedules.Retrieve(
  context.TODO(), "{{SUBSCRIPTIONSCHEDULE_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.SubscriptionSchedules;
SubscriptionSchedule subscriptionSchedule = service.Get(
    "{{SUBSCRIPTIONSCHEDULE_ID}}");
```

To update the subscription schedule after you obtain it, remove the `automatic_tax[enabled]=false` parameter, and pass down the other phases and parameters:

```curl
curl https://api.stripe.com/v1/subscription_schedules/{{SUBSCRIPTIONSCHEDULE_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "phases[0][items][0][price]"=price_1GqNdGAJVYItwOKqEHb \
  -d "phases[0][items][0][quantity]"=1 \
  -d "phases[0][start_date]"=1577865600 \
  -d "phases[0][end_date]"=1578038400 \
  -d "phases[1][items][0][price]"=price_1GqNdGAJVYItwOKqEHb \
  -d "phases[1][items][0][quantity]"=2 \
  -d "phases[1][start_date]"=1578038400 \
  -d "phases[1][end_date]"=1580544000 \
  -d "default_settings[automatic_tax][enabled]"=true
```

```cli
stripe subscription_schedules update {{SUBSCRIPTIONSCHEDULE_ID}} \
  -d "phases[0][items][0][price]"=price_1GqNdGAJVYItwOKqEHb \
  -d "phases[0][items][0][quantity]"=1 \
  -d "phases[0][start_date]"=1577865600 \
  -d "phases[0][end_date]"=1578038400 \
  -d "phases[1][items][0][price]"=price_1GqNdGAJVYItwOKqEHb \
  -d "phases[1][items][0][quantity]"=2 \
  -d "phases[1][start_date]"=1578038400 \
  -d "phases[1][end_date]"=1580544000 \
  -d "default_settings[automatic_tax][enabled]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

subscription_schedule = client.v1.subscription_schedules.update(
  '{{SUBSCRIPTIONSCHEDULE_ID}}',
  {
    phases: [
      {
        items: [
          {
            price: 'price_1GqNdGAJVYItwOKqEHb',
            quantity: 1,
          },
        ],
        start_date: 1577865600,
        end_date: 1578038400,
      },
      {
        items: [
          {
            price: 'price_1GqNdGAJVYItwOKqEHb',
            quantity: 2,
          },
        ],
        start_date: 1578038400,
        end_date: 1580544000,
      },
    ],
    default_settings: {automatic_tax: {enabled: true}},
  },
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
subscription_schedule = client.v1.subscription_schedules.update(
  "{{SUBSCRIPTIONSCHEDULE_ID}}",
  {
    "phases": [
      {
        "items": [{"price": "price_1GqNdGAJVYItwOKqEHb", "quantity": 1}],
        "start_date": 1577865600,
        "end_date": 1578038400,
      },
      {
        "items": [{"price": "price_1GqNdGAJVYItwOKqEHb", "quantity": 2}],
        "start_date": 1578038400,
        "end_date": 1580544000,
      },
    ],
    "default_settings": {"automatic_tax": {"enabled": True}},
  },
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$subscriptionSchedule = $stripe->subscriptionSchedules->update(
  '{{SUBSCRIPTIONSCHEDULE_ID}}',
  [
    'phases' => [
      [
        'items' => [
          [
            'price' => 'price_1GqNdGAJVYItwOKqEHb',
            'quantity' => 1,
          ],
        ],
        'start_date' => 1577865600,
        'end_date' => 1578038400,
      ],
      [
        'items' => [
          [
            'price' => 'price_1GqNdGAJVYItwOKqEHb',
            'quantity' => 2,
          ],
        ],
        'start_date' => 1578038400,
        'end_date' => 1580544000,
      ],
    ],
    'default_settings' => ['automatic_tax' => ['enabled' => true]],
  ]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SubscriptionScheduleUpdateParams params =
  SubscriptionScheduleUpdateParams.builder()
    .addPhase(
      SubscriptionScheduleUpdateParams.Phase.builder()
        .addItem(
          SubscriptionScheduleUpdateParams.Phase.Item.builder()
            .setPrice("price_1GqNdGAJVYItwOKqEHb")
            .setQuantity(1L)
            .build()
        )
        .setStartDate(1577865600L)
        .setEndDate(1578038400L)
        .build()
    )
    .addPhase(
      SubscriptionScheduleUpdateParams.Phase.builder()
        .addItem(
          SubscriptionScheduleUpdateParams.Phase.Item.builder()
            .setPrice("price_1GqNdGAJVYItwOKqEHb")
            .setQuantity(2L)
            .build()
        )
        .setStartDate(1578038400L)
        .setEndDate(1580544000L)
        .build()
    )
    .setDefaultSettings(
      SubscriptionScheduleUpdateParams.DefaultSettings.builder()
        .setAutomaticTax(
          SubscriptionScheduleUpdateParams.DefaultSettings.AutomaticTax.builder()
            .setEnabled(true)
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
SubscriptionSchedule subscriptionSchedule =
  client.v1().subscriptionSchedules().update(
    "{{SUBSCRIPTIONSCHEDULE_ID}}",
    params
  );
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const subscriptionSchedule = await stripe.subscriptionSchedules.update(
  '{{SUBSCRIPTIONSCHEDULE_ID}}',
  {
    phases: [
      {
        items: [
          {
            price: 'price_1GqNdGAJVYItwOKqEHb',
            quantity: 1,
          },
        ],
        start_date: 1577865600,
        end_date: 1578038400,
      },
      {
        items: [
          {
            price: 'price_1GqNdGAJVYItwOKqEHb',
            quantity: 2,
          },
        ],
        start_date: 1578038400,
        end_date: 1580544000,
      },
    ],
    default_settings: {
      automatic_tax: {
        enabled: true,
      },
    },
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SubscriptionScheduleUpdateParams{
  Phases: []*stripe.SubscriptionScheduleUpdatePhaseParams{
    &stripe.SubscriptionScheduleUpdatePhaseParams{
      Items: []*stripe.SubscriptionScheduleUpdatePhaseItemParams{
        &stripe.SubscriptionScheduleUpdatePhaseItemParams{
          Price: stripe.String("price_1GqNdGAJVYItwOKqEHb"),
          Quantity: stripe.Int64(1),
        },
      },
      StartDate: stripe.Int64(1577865600),
      EndDate: stripe.Int64(1578038400),
    },
    &stripe.SubscriptionScheduleUpdatePhaseParams{
      Items: []*stripe.SubscriptionScheduleUpdatePhaseItemParams{
        &stripe.SubscriptionScheduleUpdatePhaseItemParams{
          Price: stripe.String("price_1GqNdGAJVYItwOKqEHb"),
          Quantity: stripe.Int64(2),
        },
      },
      StartDate: stripe.Int64(1578038400),
      EndDate: stripe.Int64(1580544000),
    },
  },
  DefaultSettings: &stripe.SubscriptionScheduleUpdateDefaultSettingsParams{
    AutomaticTax: &stripe.SubscriptionAutomaticTaxParams{Enabled: stripe.Bool(true)},
  },
}
result, err := sc.V1SubscriptionSchedules.Update(
  context.TODO(), "{{SUBSCRIPTIONSCHEDULE_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new SubscriptionScheduleUpdateOptions
{
    Phases = new List<SubscriptionSchedulePhaseOptions>
    {
        new SubscriptionSchedulePhaseOptions
        {
            Items = new List<SubscriptionSchedulePhaseItemOptions>
            {
                new SubscriptionSchedulePhaseItemOptions
                {
                    Price = "price_1GqNdGAJVYItwOKqEHb",
                    Quantity = 1,
                },
            },
            StartDate = DateTimeOffset.FromUnixTimeSeconds(1577865600).UtcDateTime,
            EndDate = DateTimeOffset.FromUnixTimeSeconds(1578038400).UtcDateTime,
        },
        new SubscriptionSchedulePhaseOptions
        {
            Items = new List<SubscriptionSchedulePhaseItemOptions>
            {
                new SubscriptionSchedulePhaseItemOptions
                {
                    Price = "price_1GqNdGAJVYItwOKqEHb",
                    Quantity = 2,
                },
            },
            StartDate = DateTimeOffset.FromUnixTimeSeconds(1578038400).UtcDateTime,
            EndDate = DateTimeOffset.FromUnixTimeSeconds(1580544000).UtcDateTime,
        },
    },
    DefaultSettings = new SubscriptionScheduleDefaultSettingsOptions
    {
        AutomaticTax = new SubscriptionScheduleDefaultSettingsAutomaticTaxOptions
        {
            Enabled = true,
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.SubscriptionSchedules;
SubscriptionSchedule subscriptionSchedule = service.Update(
    "{{SUBSCRIPTIONSCHEDULE_ID}}",
    options);
```

#### Schedule the update

If you want to avoid creating a prorated item, you can schedule the update to occur at the start of the next cycle.

You can currently only schedule subscription updates with the API:

#### Ruby

```ruby

# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
Stripe.api_key = '<<YOUR_SECRET_KEY>>'

subscription = Stripe::Subscription.retrieve(
  '{{SUBSCRIPTION_ID}}',
)
schedule = Stripe::SubscriptionSchedule.create({
  from_subscription: subscription.id
})
Stripe::SubscriptionSchedule.update(
  schedule.id,
  {
    end_behavior: 'release',
    phases: [
      # The first phase contains items for the
      # latest subscription invoice
      {
        items: [
          # Prices and tax_rates for each item
          {
            price: '{{PRICE_ID}}',
            tax_rates: [
              '{{TAX_RATE_ID}}'
            ]
          }
        ],
        default_tax_rates: ['{{TAX_RATE_ID}}'],
        start_date: subscription.items.data[0].current_period_start,
        end_date: subscription.items.data[0].current_period_end
      },
      # The second phase removes manual tax rates and enables
      # automatic tax calculation
      {
        items: [
          # Prices for each item with tax_rates: ''
          {
            price: '{{PRICE_ID}}',
            tax_rates: ''
          }
        ],
        default_tax_rates: '',
        automatic_tax: {enabled: true},
        iterations: 1
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

subscription = stripe.Subscription.retrieve('{{SUBSCRIPTION_ID}}')
schedule = stripe.SubscriptionSchedule.create(
    from_subscription=subscription['id'],
)
updated_schedule = stripe.SubscriptionSchedule.modify(
  schedule['id'],
  end_behavior = "release",
  phases=[
    # The first phase contains items for the
    # latest subscription invoice
    {
      "items": [
        # Prices and tax_rates for each item
        {
          "price": "{{PRICE_ID}}",
          "tax_rates": [
              "{{TAX_RATE_ID}}",
          ]
        },
      ],
      "default_tax_rates": ["{{TAX_RATE_ID}}"],
      "start_date": subscription['items']['data'][0].current_period_start,
      "end_date": subscription['items']['data'][0].current_period_end,
    },
    # The second phase removes manual tax rates and enables
    # automatic tax calculation
    {
      "items": [
        # Prices for each item with tax_rates: ""
        {
          "price": "{{PRICE_ID}}",
          "tax_rates": ""
        },
      ],
      "default_tax_rates": "",
      "automatic_tax": {"enabled": True},
      "iterations": 1
    }
  ],
)
```

#### PHP

```php

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
\Stripe\Stripe::setApiKey('<<YOUR_SECRET_KEY>>');

$subscription = $stripe->subscriptions->retrieve(
  '{{SUBSCRIPTION_ID}}',
  []
);
$schedule = $stripe->subscriptionSchedules->create([
  'from_subscription' => $subscription->id
]);
$updatedSchedule = $stripe->subscriptionSchedules->update(
  $schedule->id,
  [
    'end_behavior' => 'release',
    'phases' => [
      # The first phase contains items for the
      # latest subscription invoice
      [
        'items' => [
          # Prices and tax_rates for each item
          [
            'price' => '{{PRICE_ID}}',
            'tax_rates' => [
              '{{TAX_RATE_ID}}'
            ]
          ]
        ],
        'default_tax_rates' => ['{{TAX_RATE_ID}}'],
        'start_date' => $subscription->items->data[0]->current_period_start,
        'end_date' => $subscription->items->data[0]->current_period_end
      ],
      # The second phase removes manual tax rates and enables
      # automatic tax calculation
      [
        'items' => [
          # Prices for each item with tax_rates => ''
          [
            'price' => '{{PRICE_ID}}',
            'tax_rates' => ''
          ],
        ],
        'default_tax_rates' => '',
        'automatic_tax' => ['enabled' => true],
        'iterations' => 1
      ]
    ]
  ]
);
```

#### Node.js

```javascript

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const subscription = await stripe.subscriptions.retrieve(
  '{{SUBSCRIPTION_ID}}'
);
const schedule = await stripe.subscriptionSchedules.create(
  {from_subscription: subscription.id}
);
await stripe.subscriptionSchedules.update(
  schedule.id,
  {
    end_behavior: 'release',
    phases: [
      // The first phase contains items for the
      // latest subscription invoice
      {
        items: [
          // Prices and tax_rates for each item
          {
            price: '{{PRICE_ID}}',
            tax_rates: [
              '{{TAX_RATE_ID}}',
            ]
          }
        ],
        default_tax_rates: ['{{TAX_RATE_ID}}'],
        start_date: subscription.items.data[0].current_period_start,
        end_date: subscription.items.data[0].current_period_end
      },
      // The second phase removes manual tax rates and enables
      // automatic tax calculation
      {
        items: [
          // Prices for each item with tax_rates: ''
          {
            price: '{{PRICE_ID}}',
            tax_rates: ''
          },
        ],
        default_tax_rates: '',
        automatic_tax: {enabled: true},
        iterations: 1
      }
    ]
  }
);
```

#### Java

```java

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
Stripe.apiKey = "<<YOUR_SECRET_KEY>>";

Subscription subscription = Subscription.retrieve("{{SUBSCRIPTION_ID}}");
Map<String, Object> createParams = new HashMap<>();
createParams.put("from_subscription", subscription.getId());

SubscriptionSchedule subscriptionSchedule = SubscriptionSchedule.create(createParams);

ArrayList<Object> taxRates = new ArrayList<>();
taxRates.add("{{TAX_RATE_ID}}");
// Prices and tax_rates for each item
Map<String, Object> item1 = new HashMap<>();
item1.put("price", "{{PRICE_ID}}");
item1.put("tax_rates", taxRates);
ArrayList<Object> items1 = new ArrayList<>();
items1.add(item1);
ArrayList<Object> defaultTaxRates = new ArrayList<>();
defaultTaxRates.add("{{TAX_RATE_ID}}");
// The first phase contains items for the
// latest subscription invoice
Map<String, Object> phase1 = new HashMap<>();
phase1.put("items", items1);
phase1.put("default_tax_rates", defaultTaxRates);
phase1.put("start_date", subscription.getItems().getData().get(0).getCurrentPeriodStart());
phase1.put("end_date", subscription.getItems().getData().get(0).getCurrentPeriodEnd());

// Prices for each item with tax_rates: ""
Map<String, Object> item2 = new HashMap<>();
item2.put("price", "{{PRICE_ID}}");
item2.put("tax_rates", "");
ArrayList<Object> items2 = new ArrayList<>();
items2.add(item2);
Map<String, Object> automaticTax = new HashMap<>();
automaticTax.put("enabled", true);
// The second phase removes manual tax rates and enables
// automatic tax calculation
Map<String, Object> phase2 = new HashMap<>();
phase2.put("items", items2);
phase2.put("default_tax_rates", "");
phase2.put("automatic_tax", automaticTax);
phase2.put("iterations", 1);

ArrayList<Object> phases = new ArrayList<>();
phases.add(phase1);
phases.add(phase2);

Map<String, Object> updateParams = new HashMap<>();
updateParams.put("end_behavior", "release");
updateParams.put("phases", phases);

subscriptionSchedule.update(updateParams);

```

#### .NET

```dotnet

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeConfiguration.ApiKey = "<<YOUR_SECRET_KEY>>";

var subscriptionService = new SubscriptionService();
var subscription = subscriptionService.Get("{{SUBSCRIPTION_ID}}");
var scheduleService = new SubscriptionScheduleService();
var schedule = scheduleService.Create(new SubscriptionScheduleCreateOptions
  {
    FromSubscription = subscription.Id
  }
);
var options = new SubscriptionScheduleUpdateOptions
{
  EndBehavior = "release",
  Phases = new List<SubscriptionSchedulePhaseOptions>
  {
    // The first phase contains items for the
    // latest subscription invoice
    new SubscriptionSchedulePhaseOptions
    {
      Items = new List<SubscriptionSchedulePhaseItemOptions>
      {
        //  Prices and tax_rates for each item
        new SubscriptionSchedulePhaseItemOptions
        {
          Price = "{{PRICE_ID}}",
          TaxRates = new List<String>{"{{TAX_RATE_ID}}"},
        },
      },
      DefaultTaxRates = new List<String>{"{{TAX_RATE_ID}}"},
      StartDate = subscription.Items.Data[0].CurrentPeriodStart,
      EndDate = subscription.Items.Data[0].CurrentPeriodEnd,
    },
    // The second phase removes manual tax rates and enables
    // automatic tax calculation
    new SubscriptionSchedulePhaseOptions
    {
      Items = new List<SubscriptionSchedulePhaseItemOptions>
      {
        // Prices for each item with TaxRates = new List<String>()
        new SubscriptionSchedulePhaseItemOptions
        {
          Price = "{{PRICE_ID}}",
          TaxRates = new List<String>(),
        },
      },
      DefaultTaxRates = new List<String>(),
      AutomaticTax = new SubscriptionSchedulePhaseAutomaticTaxOptions
      {
        Enabled = true
      },
      Iterations = 1,
    },
  },
};
scheduleService.Update(
  schedule.Id,
  options
);
```

#### Go

```go

// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
stripe.Key = "<<YOUR_SECRET_KEY>>"

subscription, _ := subscription.Get("{{SUBSCRIPTION_ID}}", nil)
schedule, _ := subscriptionschedule.New(&stripe.SubscriptionScheduleParams{
  FromSubscription: stripe.String(subscription.ID),
})

params := &stripe.SubscriptionScheduleParams{
  EndBehavior: stripe.String("release"),
  Phases: []*stripe.SubscriptionSchedulePhaseParams{
    // The first phase contains items for the
    // latest subscription invoice
    {
      Items: []*stripe.SubscriptionSchedulePhaseItemParams{
        //  Prices and tax_rates for each item
        {
          Price:    stripe.String("{{PRICE_ID}}"),
          TaxRates: stripe.StringSlice([]string{"{{TAX_RATE_ID}}"}),
        },
      },
      DefaultTaxRates: stripe.StringSlice([]string{"{{TAX_RATE_ID}}"}),
      StartDate:       stripe.Int64(subscription.Items.Data[0].CurrentPeriodStart),
      EndDate:         stripe.Int64(subscription.Items.Data[0].CurrentPeriodEnd),
    },
    // The second phase removes manual tax rates and enables
    // automatic tax calculation
    {
      Items: []*stripe.SubscriptionSchedulePhaseItemParams{
        // Prices for each item with TaxRates: stripe.StringSlice(nil)
        {
          Price:    stripe.String("{{PRICE_ID}}"),
          TaxRates: stripe.StringSlice(nil),
        },
      },
      DefaultTaxRates: stripe.StringSlice(nil),
      AutomaticTax: &stripe.SubscriptionSchedulePhaseAutomaticTaxParams{
        Enabled: stripe.Bool(true),
      },
      Iterations: stripe.Int64(1),
    },
  },
}

subscriptionschedule.Update(
  schedule.ID,
  params,
)

s, _ := subscription.New(params)
```

## Confirm updates

To confirm that you’ve properly updated your subscriptions, create a [preview invoice](https://docs.stripe.com/api/invoices/create_preview.md) for each subscription and inspect the results of its tax calculation.

You can retrieve the tax amounts from the [tax](https://docs.stripe.com/api/invoices/object.md#invoice_object-tax) and [total_tax_amounts](https://docs.stripe.com/api/invoices/object.md#invoice_object-total_tax_amounts) fields on the preview invoice, and from the per-line-item [tax_amounts](https://docs.stripe.com/api/invoices/line_item.md#invoice_line_item_object-tax_amounts) fields. The invoice has an [automatic_tax](https://docs.stripe.com/api/invoices/object.md#invoice_object-automatic_tax) field showing the status of the calculation, with one of three possible statuses:

| Status                     | Description                                                                                                           | Possible Action                                                                                                                                                                                   |
| -------------------------- | --------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `complete`                 | Stripe Tax has successfully assessed the taxes on the payment.                                                        | You can retrieve the tax amounts from the tax and `total_tax_amounts` fields on the latest invoice, and from the per-line item `tax_amounts` fields.                                              |
| `requires_location_inputs` | Stripe Tax was unable to assess taxes because it didn’t have enough information to determine the customer’s location. | Collect more information from a customer (such as a full street address) and update the [customer.address](https://docs.stripe.com/api/invoices/object.md#invoice_object-customer_address) field. |
| `failed`                   | Internal Stripe error.                                                                                                | Try the request again or contact Stripe support for additional assistance.                                                                                                                        |

## See also

- [Create new subscriptions with Stripe Tax](https://docs.stripe.com/tax/subscriptions.md)
- [Setting tax codes, products, and prices](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md)
