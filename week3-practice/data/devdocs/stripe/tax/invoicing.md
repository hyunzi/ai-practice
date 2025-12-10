# Automatically collect tax on invoices

Learn how to automatically calculate tax on your invoices.

On an *invoice* (Invoices are statements of amounts owed by a customer. They track the status of payments from draft through paid or otherwise finalized. Subscriptions automatically generate invoices, or you can manually create a one-off invoice), Stripe Tax calculates sales tax, VAT, and GST. To calculate these for each line item, Stripe uses:

- Your [tax settings](https://dashboard.stripe.com/settings/tax)
- The customer’s tax settings and location
- The product tax code and price tax behavior

Stripe calculates the tax at the published rate at the time of invoice finalization. For example, if you initialize an invoice draft on December 20 and set it to finalize on January 15, then January 1 introduces a new tax rate. Subsequently, Stripe calculates the invoice at the new rate.
[Watch on YouTube](https://www.youtube.com/watch?v=_A2tC63ooSs)
## Set up the customer

We use the customer’s location to determine the relevant taxes to collect. Different countries have different setup requirements:

- Customers outside of the US require at least a country-level address.
- Customers in the US require a 5-digit postal code.
- Customers in Canada require either the province or postal code.

#### Dashboard

You can add customer location information in the **Customer details** page by clicking **Edit** next to **Details**. To add a customer’s location from the [Invoice Editor](https://dashboard.stripe.com/invoices/create), click the overflow menu (⋯) next to the customer. Select **Edit customer information**, click **Add additional details**, and scroll down to **Billing details**.

After you update the location, click **Update customer**. Stripe applies the new location to all of your customer’s future invoices unless you update it. For more information, see [Determine customer locations](https://docs.stripe.com/tax/customer-locations.md).

#### API

To create a customer with the API, send Stripe a description, and as much information as possible to help identify the location and tax requirements for your customer. We recommend populating the [customer.address](https://docs.stripe.com/api/customers/create.md#create_customer-address) field with your customer’s complete billing address. Validate the customer address upon creation by passing [tax[validate_location]=“immediately”](https://docs.stripe.com/api/customers/create.md#create_customer-tax-validate_location). You can also [expand](https://docs.stripe.com/api/expanding_objects.md) the [tax](https://docs.stripe.com/api/customers/create.md#create_customer-tax) field to confirm the location Stripe Tax identified for your customer.

```curl
curl https://api.stripe.com/v1/customers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d description="a new user" \
  -d "address[line1]"="510 Townsend St" \
  -d "address[city]"="San Francisco" \
  -d "address[state]"=CA \
  -d "address[country]"=US \
  -d "address[postal_code]"=94103 \
  -d "tax[validate_location]"=immediately \
  -d "expand[]"=tax
```

```cli
stripe customers create  \
  --description="a new user" \
  -d "address[line1]"="510 Townsend St" \
  -d "address[city]"="San Francisco" \
  -d "address[state]"=CA \
  -d "address[country]"=US \
  -d "address[postal_code]"=94103 \
  -d "tax[validate_location]"=immediately \
  -d "expand[0]"=tax
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

customer = client.v1.customers.create({
  description: 'a new user',
  address: {
    line1: '510 Townsend St',
    city: 'San Francisco',
    state: 'CA',
    country: 'US',
    postal_code: '94103',
  },
  tax: {validate_location: 'immediately'},
  expand: ['tax'],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
customer = client.v1.customers.create({
  "description": "a new user",
  "address": {
    "line1": "510 Townsend St",
    "city": "San Francisco",
    "state": "CA",
    "country": "US",
    "postal_code": "94103",
  },
  "tax": {"validate_location": "immediately"},
  "expand": ["tax"],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$customer = $stripe->customers->create([
  'description' => 'a new user',
  'address' => [
    'line1' => '510 Townsend St',
    'city' => 'San Francisco',
    'state' => 'CA',
    'country' => 'US',
    'postal_code' => '94103',
  ],
  'tax' => ['validate_location' => 'immediately'],
  'expand' => ['tax'],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CustomerCreateParams params =
  CustomerCreateParams.builder()
    .setDescription("a new user")
    .setAddress(
      CustomerCreateParams.Address.builder()
        .setLine1("510 Townsend St")
        .setCity("San Francisco")
        .setState("CA")
        .setCountry("US")
        .setPostalCode("94103")
        .build()
    )
    .setTax(
      CustomerCreateParams.Tax.builder()
        .setValidateLocation(CustomerCreateParams.Tax.ValidateLocation.IMMEDIATELY)
        .build()
    )
    .addExpand("tax")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Customer customer = client.v1().customers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const customer = await stripe.customers.create({
  description: 'a new user',
  address: {
    line1: '510 Townsend St',
    city: 'San Francisco',
    state: 'CA',
    country: 'US',
    postal_code: '94103',
  },
  tax: {
    validate_location: 'immediately',
  },
  expand: ['tax'],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CustomerCreateParams{
  Description: stripe.String("a new user"),
  Address: &stripe.AddressParams{
    Line1: stripe.String("510 Townsend St"),
    City: stripe.String("San Francisco"),
    State: stripe.String("CA"),
    Country: stripe.String("US"),
    PostalCode: stripe.String("94103"),
  },
  Tax: &stripe.CustomerCreateTaxParams{ValidateLocation: stripe.String("immediately")},
}
params.AddExpand("tax")
result, err := sc.V1Customers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CustomerCreateOptions
{
    Description = "a new user",
    Address = new AddressOptions
    {
        Line1 = "510 Townsend St",
        City = "San Francisco",
        State = "CA",
        Country = "US",
        PostalCode = "94103",
    },
    Tax = new CustomerTaxOptions { ValidateLocation = "immediately" },
    Expand = new List<string> { "tax" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Customers;
Customer customer = service.Create(options);
```

You can also add both your customer’s country and postal code:

```curl
curl https://api.stripe.com/v1/customers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d description="a new user" \
  -d "address[country]"=US \
  -d "address[postal_code]"=94103 \
  -d "tax[validate_location]"=immediately \
  -d "expand[]"=tax
```

```cli
stripe customers create  \
  --description="a new user" \
  -d "address[country]"=US \
  -d "address[postal_code]"=94103 \
  -d "tax[validate_location]"=immediately \
  -d "expand[0]"=tax
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

customer = client.v1.customers.create({
  description: 'a new user',
  address: {
    country: 'US',
    postal_code: '94103',
  },
  tax: {validate_location: 'immediately'},
  expand: ['tax'],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
customer = client.v1.customers.create({
  "description": "a new user",
  "address": {"country": "US", "postal_code": "94103"},
  "tax": {"validate_location": "immediately"},
  "expand": ["tax"],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$customer = $stripe->customers->create([
  'description' => 'a new user',
  'address' => [
    'country' => 'US',
    'postal_code' => '94103',
  ],
  'tax' => ['validate_location' => 'immediately'],
  'expand' => ['tax'],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CustomerCreateParams params =
  CustomerCreateParams.builder()
    .setDescription("a new user")
    .setAddress(
      CustomerCreateParams.Address.builder()
        .setCountry("US")
        .setPostalCode("94103")
        .build()
    )
    .setTax(
      CustomerCreateParams.Tax.builder()
        .setValidateLocation(CustomerCreateParams.Tax.ValidateLocation.IMMEDIATELY)
        .build()
    )
    .addExpand("tax")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Customer customer = client.v1().customers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const customer = await stripe.customers.create({
  description: 'a new user',
  address: {
    country: 'US',
    postal_code: '94103',
  },
  tax: {
    validate_location: 'immediately',
  },
  expand: ['tax'],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CustomerCreateParams{
  Description: stripe.String("a new user"),
  Address: &stripe.AddressParams{
    Country: stripe.String("US"),
    PostalCode: stripe.String("94103"),
  },
  Tax: &stripe.CustomerCreateTaxParams{ValidateLocation: stripe.String("immediately")},
}
params.AddExpand("tax")
result, err := sc.V1Customers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CustomerCreateOptions
{
    Description = "a new user",
    Address = new AddressOptions { Country = "US", PostalCode = "94103" },
    Tax = new CustomerTaxOptions { ValidateLocation = "immediately" },
    Expand = new List<string> { "tax" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Customers;
Customer customer = service.Create(options);
```

Or only their IP address:

```curl
curl https://api.stripe.com/v1/customers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d description="a new user" \
  -d "tax[ip_address]"="203.0.113.0" \
  -d "tax[validate_location]"=immediately \
  -d "expand[]"=tax
```

```cli
stripe customers create  \
  --description="a new user" \
  -d "tax[ip_address]"="203.0.113.0" \
  -d "tax[validate_location]"=immediately \
  -d "expand[0]"=tax
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

customer = client.v1.customers.create({
  description: 'a new user',
  tax: {
    ip_address: '203.0.113.0',
    validate_location: 'immediately',
  },
  expand: ['tax'],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
customer = client.v1.customers.create({
  "description": "a new user",
  "tax": {"ip_address": "203.0.113.0", "validate_location": "immediately"},
  "expand": ["tax"],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$customer = $stripe->customers->create([
  'description' => 'a new user',
  'tax' => [
    'ip_address' => '203.0.113.0',
    'validate_location' => 'immediately',
  ],
  'expand' => ['tax'],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CustomerCreateParams params =
  CustomerCreateParams.builder()
    .setDescription("a new user")
    .setTax(
      CustomerCreateParams.Tax.builder()
        .setIpAddress("203.0.113.0")
        .setValidateLocation(CustomerCreateParams.Tax.ValidateLocation.IMMEDIATELY)
        .build()
    )
    .addExpand("tax")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Customer customer = client.v1().customers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const customer = await stripe.customers.create({
  description: 'a new user',
  tax: {
    ip_address: '203.0.113.0',
    validate_location: 'immediately',
  },
  expand: ['tax'],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CustomerCreateParams{
  Description: stripe.String("a new user"),
  Tax: &stripe.CustomerCreateTaxParams{
    IPAddress: stripe.String("203.0.113.0"),
    ValidateLocation: stripe.String("immediately"),
  },
}
params.AddExpand("tax")
result, err := sc.V1Customers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CustomerCreateOptions
{
    Description = "a new user",
    Tax = new CustomerTaxOptions
    {
        IpAddress = "203.0.113.0",
        ValidateLocation = "immediately",
    },
    Expand = new List<string> { "tax" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Customers;
Customer customer = service.Create(options);
```

The expanded `tax` field indicates the computed tax location and if the customer is compatible with automatic tax calculation:

```json
{
  "id": ""{{CUSTOMER_ID}}"",
  "object": "customer",
  // ... other fields omitted
  "tax": {
    "location": {
      "country": "US",
      "state": "CA",
      "source": "billing_address"
    },
    "ip_address": null,"automatic_tax": "supported",
  }
}
```

The value of the [tax[automatic_tax]](https://docs.stripe.com/api/customers/object.md#customer_object-tax-automatic_tax) parameter has four possible states:

| Status                  | Description                                                                          | Possible Action                                                                                                                                                                                                                           |
| ----------------------- | ------------------------------------------------------------------------------------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `supported`             | Automatic tax is fully supported.                                                    | No further action needed.                                                                                                                                                                                                                 |
| `unrecognized_location` | The address isn’t valid for determining a tax location.                              | Ask the customer for an updated address and set `customer.address` to the new value.                                                                                                                                                      |
| `not_collecting`        | The address is resolvable to a location for which you haven’t set up a registration. | Depending on your tax obligations, you can either proceed and Stripe Tax won’t assess any taxes, or you might want to [add a new registration](https://docs.stripe.com/tax/registering.md) for the jurisdiction the customer is based in. |
| `failed`                | An error occurred with Stripe’s servers. This is rare.                               | Try the request again, or contact Stripe support for additional assistance.                                                                                                                                                               |

## Set up line items

To calculate tax on each line item on an invoice, you need to set a tax behavior and optionally a tax code.

### Customize tax settings for one-off line items

Customize line items in the Invoice Editor by selecting the tax behavior from the **Include tax in price** drop-down menu.
![Customize tax settings for one-off line items](https://b.stripecdn.com/docs-statics-srv/assets/invoicing_price.faa90fb6b3cb833b900e06cb2187d339.png)

Customize tax settings for one-off line items

### Customize tax settings for product-based line items 

You can use both the Dashboard and the API to customize tax settings for product-based line items.

#### Dashboard

On the [Products page](https://dashboard.stripe.com/products), you can select both the tax behavior for a particular price and the optional tax code for the product. The tax behavior is per price. You can’t change the tax behavior after you select it, but you can create new prices or archive old ones. To set up a tax behavior, click **Add a price** (or **Add another price** if you already have one) and select it from the **Tax behavior** drop-down menu.

To set up a tax code, select it from the **Tax code** drop-down menu when you create a new product or edit the details of an existing one.
![Customize tax settings for one-off line items](https://b.stripecdn.com/docs-statics-srv/assets/invoicing_new_price.517f186f27925e52e501019b9aecc94b.png)

Customize tax settings for one-off line items

#### API

Stripe Tax uses information stored on the [Products](https://docs.stripe.com/api/products.md) and [Prices](https://docs.stripe.com/api/prices.md) objects to determine the rates and rules to apply when calculating tax. Update the products and prices you use with Invoices to include:

- [Tax behavior](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md#tax-behavior)—either *inclusive* or *exclusive*. This determines whether or not tax is already included in your pricing. For example, an inclusive line item with an amount of 10 USD totals to 10 USD, whereas an exclusive line item with an amount of 10 USD totals to 10 USD plus tax. Exclusive pricing is common practice in US markets and for B2B sales, while inclusive is common practice for B2C buyers in many markets outside the US. Setting the tax behavior explicitly on a price is optional, if you [set up the default tax behavior](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md#setting-tax-behavior-on-a-price-\(optional\)) in the [Stripe Tax settings](https://dashboard.stripe.com/login?redirect=%2Fsettings%2Ftax). You can override the default tax behavior setting by setting a tax behavior on a price.

- [Tax code](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md) (Optional)—a selection from a list of options that determine what type of product it is. Some examples include “Audio book," “Gift card," or “Software as a service." If you don’t set this explicitly, your preset tax code applies.

> You can’t change `tax_behavior` after it’s set to either *exclusive* or *inclusive*. If you want to change the tax behavior of a price, you need to create a new price with the desired behavior, and archive the old price.

```curl
curl https://api.stripe.com/v1/prices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d unit_amount=5000 \
  -d currency=usd \
  -d tax_behavior=exclusive \
  -d "product_data[name]"="A new product"
```

```cli
stripe prices create  \
  --unit-amount=5000 \
  --currency=usd \
  --tax-behavior=exclusive \
  -d "product_data[name]"="A new product"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

price = client.v1.prices.create({
  unit_amount: 5000,
  currency: 'usd',
  tax_behavior: 'exclusive',
  product_data: {name: 'A new product'},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
price = client.v1.prices.create({
  "unit_amount": 5000,
  "currency": "usd",
  "tax_behavior": "exclusive",
  "product_data": {"name": "A new product"},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$price = $stripe->prices->create([
  'unit_amount' => 5000,
  'currency' => 'usd',
  'tax_behavior' => 'exclusive',
  'product_data' => ['name' => 'A new product'],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PriceCreateParams params =
  PriceCreateParams.builder()
    .setUnitAmount(5000L)
    .setCurrency("usd")
    .setTaxBehavior(PriceCreateParams.TaxBehavior.EXCLUSIVE)
    .setProductData(
      PriceCreateParams.ProductData.builder().setName("A new product").build()
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
  unit_amount: 5000,
  currency: 'usd',
  tax_behavior: 'exclusive',
  product_data: {
    name: 'A new product',
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PriceCreateParams{
  UnitAmount: stripe.Int64(5000),
  Currency: stripe.String(stripe.CurrencyUSD),
  TaxBehavior: stripe.String(stripe.PriceTaxBehaviorExclusive),
  ProductData: &stripe.PriceCreateProductDataParams{Name: stripe.String("A new product")},
}
result, err := sc.V1Prices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PriceCreateOptions
{
    UnitAmount = 5000,
    Currency = "usd",
    TaxBehavior = "exclusive",
    ProductData = new PriceProductDataOptions { Name = "A new product" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Prices;
Price price = service.Create(options);
```

## Enable automatic tax

#### Dashboard

> [Log in](https://dashboard.stripe.com/settings/tax) or [sign up](https://dashboard.stripe.com/register) for Stripe to enable Stripe Tax.

Enable the **Use automatic tax collection** toggle on the [tax settings](https://dashboard.stripe.com/settings/tax/integrations) page to automatically enable tax calculation on *new* invoices you create in the Dashboard.
![Stripe Dashboard with the automatic tax toggle set to true](https://b.stripecdn.com/docs-statics-srv/assets/dashboard_automatic_tax.2338adf39e3a07ad9acd79c036e7c637.png)

Stripe Dashboard with the automatic tax toggle set to true

### Update untaxed invoices 

To enable automatic tax calculation for existing invoices:

1. Click **Edit invoice** from the **Invoice details** page, or click the invoice’s overflow menu (⋯), then **Edit invoice** from the [Invoices page](https://dashboard.stripe.com/test/invoices) to create a new draft in the **Invoice Editor**.
1. In the editor, turn on the **Collect tax automatically** toggle.
1. If customer is missing address information required for tax calculation, a notification badge alerts you and provides instructions to resolve the problem.![Invoice editor with the warning badge about missing customer address](https://b.stripecdn.com/docs-statics-srv/assets/invoice_no_address_badge.cf9ee01e250675ea8742a948ddbd59d6.png)
   
   Invoice editor with the warning badge about missing customer address

1. Save the invoice to enable automatic tax calculations on all future instances of the invoice. Learn more about [editing invoices after finalization](https://docs.stripe.com/invoicing/invoice-edits.md).

### Update invoices with existing tax rates 

To replace invoice [tax rates](https://docs.stripe.com/billing/taxes/tax-rates.md) with automatic tax calculation, follow the previous steps to edit the invoice. Then remove the applied tax rates and enable the **Collect tax automatically** toggle.

#### API

After specifying a tax behavior and tax code, you can add the price to the customer as an invoice item:

```curl
curl https://api.stripe.com/v1/invoiceitems \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "pricing[price]"="{{PRICE_ID}}"
```

```cli
stripe invoiceitems create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "pricing[price]"="{{PRICE_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice_item = client.v1.invoice_items.create({
  customer: '{{CUSTOMER_ID}}',
  pricing: {price: '{{PRICE_ID}}'},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice_item = client.v1.invoice_items.create({
  "customer": "{{CUSTOMER_ID}}",
  "pricing": {"price": "{{PRICE_ID}}"},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoiceItem = $stripe->invoiceItems->create([
  'customer' => '{{CUSTOMER_ID}}',
  'pricing' => ['price' => '{{PRICE_ID}}'],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceItemCreateParams params =
  InvoiceItemCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .setPricing(
      InvoiceItemCreateParams.Pricing.builder().setPrice("{{PRICE_ID}}").build()
    )
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
  pricing: {
    price: '{{PRICE_ID}}',
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceItemCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  Pricing: &stripe.InvoiceItemCreatePricingParams{
    Price: stripe.String("{{PRICE_ID}}"),
  },
}
result, err := sc.V1InvoiceItems.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceItemCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    Pricing = new InvoiceItemPricingOptions { Price = "{{PRICE_ID}}" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.InvoiceItems;
InvoiceItem invoiceItem = service.Create(options);
```

Set the toggle in the **Invoice Editor**. In the API, you need to pass the `automatic_tax` field to enable or disable automatic tax calculation. Both steps are required to start calculating tax automatically.

```curl
curl https://api.stripe.com/v1/invoices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true
```

```cli
stripe invoices create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "automatic_tax[enabled]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.create({
  customer: '{{CUSTOMER_ID}}',
  automatic_tax: {enabled: true},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.create({
  "customer": "{{CUSTOMER_ID}}",
  "automatic_tax": {"enabled": True},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->create([
  'customer' => '{{CUSTOMER_ID}}',
  'automatic_tax' => ['enabled' => true],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceCreateParams params =
  InvoiceCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .setAutomaticTax(InvoiceCreateParams.AutomaticTax.builder().setEnabled(true).build())
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Invoice invoice = client.v1().invoices().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const invoice = await stripe.invoices.create({
  customer: '{{CUSTOMER_ID}}',
  automatic_tax: {
    enabled: true,
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  AutomaticTax: &stripe.InvoiceCreateAutomaticTaxParams{Enabled: stripe.Bool(true)},
}
result, err := sc.V1Invoices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    AutomaticTax = new InvoiceAutomaticTaxOptions { Enabled = true },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.Create(options);
```

To enable automatic tax calculation when you update an invoice, add the `invoice` parameter alongside `automatic_tax`:

```curl
curl https://api.stripe.com/v1/invoices/{{INVOICE_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "automatic_tax[enabled]"=true
```

```cli
stripe invoices update {{INVOICE_ID}} \
  -d "automatic_tax[enabled]"=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.update(
  '{{INVOICE_ID}}',
  {automatic_tax: {enabled: true}},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.update(
  "{{INVOICE_ID}}",
  {"automatic_tax": {"enabled": True}},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->update(
  '{{INVOICE_ID}}',
  ['automatic_tax' => ['enabled' => true]]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceUpdateParams params =
  InvoiceUpdateParams.builder()
    .setAutomaticTax(InvoiceUpdateParams.AutomaticTax.builder().setEnabled(true).build())
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Invoice invoice = client.v1().invoices().update("{{INVOICE_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const invoice = await stripe.invoices.update(
  '{{INVOICE_ID}}',
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
params := &stripe.InvoiceUpdateParams{
  AutomaticTax: &stripe.InvoiceUpdateAutomaticTaxParams{Enabled: stripe.Bool(true)},
}
result, err := sc.V1Invoices.Update(context.TODO(), "{{INVOICE_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceUpdateOptions
{
    AutomaticTax = new InvoiceAutomaticTaxOptions { Enabled = true },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.Update("{{INVOICE_ID}}", options);
```

## See also

- [Determine customer locations](https://docs.stripe.com/tax/customer-locations.md)
- [Understand zero tax amounts](https://docs.stripe.com/tax/zero-tax.md)
- [Reporting and filing](https://docs.stripe.com/tax/reports.md)
- [Use Stripe Tax with Connect](https://docs.stripe.com/tax/connect.md)
- [Calculate tax in your custom checkout flow](https://docs.stripe.com/tax/custom.md)
