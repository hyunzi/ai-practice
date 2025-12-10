# Integrate with the Invoicing API

Learn how to create and send an invoice with code.

The [Dashboard](https://dashboard.stripe.com/invoices) is the most common way to [create invoices](https://docs.stripe.com/invoicing/dashboard.md#create-invoice). If you’d like to automate invoice creation, you can integrate with the API. Build a full, working Invoicing integration using our [sample integration](https://docs.stripe.com/invoicing/integration/quickstart.md).

> You don’t need to integrate with the Payments API to integrate with the Invoicing API.

## Set up Stripe

Use our official libraries for access to the Stripe API:

#### Ruby

```bash
# Available as a gem
sudo gem install stripe
```

```ruby
# If you use bundler, you can add this line to your Gemfile
gem 'stripe'
```

#### Python

```bash
# Install through pip
pip3 install --upgrade stripe
```

```bash
# Or find the Stripe package on http://pypi.python.org/pypi/stripe/
```

```python
# Find the version you want to pin:
# https://github.com/stripe/stripe-python/blob/master/CHANGELOG.md
# Specify that version in your requirements.txt file
stripe>=5.0.0
```

#### PHP

```bash
# Install the PHP library with Composer
composer require stripe/stripe-php
```

```bash
# Or download the source directly: https://github.com/stripe/stripe-php/releases
```

#### Java

```java
/*
  For Gradle, add the following dependency to your build.gradle and replace with
  the version number you want to use from:
  - https://mvnrepository.com/artifact/com.stripe/stripe-java or
  - https://github.com/stripe/stripe-java/releases/latest
*/
implementation "com.stripe:stripe-java:30.0.0"
```

```xml
<!--
  For Maven, add the following dependency to your POM and replace with the
  version number you want to use from:
  - https://mvnrepository.com/artifact/com.stripe/stripe-java or
  - https://github.com/stripe/stripe-java/releases/latest
-->
<dependency>
  <groupId>com.stripe</groupId>
  <artifactId>stripe-java</artifactId>
  <version>30.0.0</version>
</dependency>
```

```bash
# For other environments, manually install the following JARs:
# - The Stripe JAR from https://github.com/stripe/stripe-java/releases/latest
# - Google Gson from https://github.com/google/gson
```

#### Node.js

```bash
# Install with npm
npm install stripe --save
```

#### Go

```bash
# Make sure your project is using Go Modules
go mod init
# Install stripe-go
go get -u github.com/stripe/stripe-go/v83
```

```go
// Then import the package
import (
  "github.com/stripe/stripe-go/v83"
)
```

#### .NET

```bash
# Install with dotnet
dotnet add package Stripe.net
dotnet restore
```

```bash
# Or install with NuGet
Install-Package Stripe.net
```

## Create a product

To create a product, enter its name:

```curl
curl https://api.stripe.com/v1/products \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d name="Gold Special"
```

```cli
stripe products create  \
  --name="Gold Special"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

product = client.v1.products.create({name: 'Gold Special'})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
product = client.v1.products.create({"name": "Gold Special"})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$product = $stripe->products->create(['name' => 'Gold Special']);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

ProductCreateParams params =
  ProductCreateParams.builder().setName("Gold Special").build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Product product = client.v1().products().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const product = await stripe.products.create({
  name: 'Gold Special',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.ProductCreateParams{Name: stripe.String("Gold Special")}
result, err := sc.V1Products.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new ProductCreateOptions { Name = "Gold Special" };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Products;
Product product = service.Create(options);
```

## Create a price

[Prices](https://docs.stripe.com/api.md#prices) define how much and how often to charge for products. This includes how much the product costs, what currency to use, and the billing interval (when the price is for a subscription). Like products, if you only have a few prices, it’s preferable to manage them in the Dashboard. Use the unit amount to express prices in the lowest unit of the currency—in this case, cents (10 USD is 1,000 cents, so the unit amount is 1000).

> As an alternative, if you don’t need to create a price for your product, you can use the [amount](https://docs.stripe.com/api/invoiceitems/create.md#create_invoiceitem-amount) parameter during invoice item creation.

To create a price and assign it to the product, pass the product ID, unit amount, and currency. In the following example, the price for the “Gold Special” product is 10 USD:

```curl
curl https://api.stripe.com/v1/prices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d product="{{PRODUCT_ID}}" \
  -d unit_amount=1000 \
  -d currency=usd
```

```cli
stripe prices create  \
  --product="{{PRODUCT_ID}}" \
  --unit-amount=1000 \
  --currency=usd
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

price = client.v1.prices.create({
  product: '{{PRODUCT_ID}}',
  unit_amount: 1000,
  currency: 'usd',
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
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Prices;
Price price = service.Create(options);
```

## Create a customer

The [Customer](https://docs.stripe.com/api.md#customer_object) object represents the customer purchasing your product. It’s required for creating an invoice. To create a customer with a `name`, `email`, and `description`, add the following code replacing the values with your own:

```curl
curl https://api.stripe.com/v1/customers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d name="Jenny Rosen" \
  --data-urlencode email="jenny.rosen@example.com" \
  -d description="My first customer"
```

```cli
stripe customers create  \
  --name="Jenny Rosen" \
  --email="jenny.rosen@example.com" \
  --description="My first customer"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

customer = client.v1.customers.create({
  name: 'Jenny Rosen',
  email: 'jenny.rosen@example.com',
  description: 'My first customer',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
customer = client.v1.customers.create({
  "name": "Jenny Rosen",
  "email": "jenny.rosen@example.com",
  "description": "My first customer",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$customer = $stripe->customers->create([
  'name' => 'Jenny Rosen',
  'email' => 'jenny.rosen@example.com',
  'description' => 'My first customer',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CustomerCreateParams params =
  CustomerCreateParams.builder()
    .setName("Jenny Rosen")
    .setEmail("jenny.rosen@example.com")
    .setDescription("My first customer")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Customer customer = client.v1().customers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const customer = await stripe.customers.create({
  name: 'Jenny Rosen',
  email: 'jenny.rosen@example.com',
  description: 'My first customer',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CustomerCreateParams{
  Name: stripe.String("Jenny Rosen"),
  Email: stripe.String("jenny.rosen@example.com"),
  Description: stripe.String("My first customer"),
}
result, err := sc.V1Customers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CustomerCreateOptions
{
    Name = "Jenny Rosen",
    Email = "jenny.rosen@example.com",
    Description = "My first customer",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Customers;
Customer customer = service.Create(options);
```

After you create the customer, store the customer `id` in your database so that you can use it later. The next step, for example, uses the customer ID to create an invoice.

> See [Create a customer](https://docs.stripe.com/api/customers/create.md) for additional parameters.

## Create an invoice

Set the [collection_method](https://docs.stripe.com/api/invoices/object.md#invoice_object-collection_method) attribute to `send_invoice`. For Stripe to mark an invoice as past due, you must add the [days_until_due](https://docs.stripe.com/api/invoices/create.md#create_invoice-days_until_due) parameter. When you send an invoice, Stripe emails the invoice to the customer with payment instructions.

```curl
curl https://api.stripe.com/v1/invoices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d collection_method=send_invoice \
  -d days_until_due=30
```

```cli
stripe invoices create  \
  --customer="{{CUSTOMER_ID}}" \
  --collection-method=send_invoice \
  --days-until-due=30
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.create({
  customer: '{{CUSTOMER_ID}}',
  collection_method: 'send_invoice',
  days_until_due: 30,
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.create({
  "customer": "{{CUSTOMER_ID}}",
  "collection_method": "send_invoice",
  "days_until_due": 30,
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->create([
  'customer' => '{{CUSTOMER_ID}}',
  'collection_method' => 'send_invoice',
  'days_until_due' => 30,
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceCreateParams params =
  InvoiceCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .setCollectionMethod(InvoiceCreateParams.CollectionMethod.SEND_INVOICE)
    .setDaysUntilDue(30L)
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
  collection_method: 'send_invoice',
  days_until_due: 30,
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  CollectionMethod: stripe.String(stripe.InvoiceCollectionMethodSendInvoice),
  DaysUntilDue: stripe.Int64(30),
}
result, err := sc.V1Invoices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    CollectionMethod = "send_invoice",
    DaysUntilDue = 30,
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.Create(options);
```

Then, create an invoice item by passing in the customer `id`, product `price`, and invoice ID `invoice`.

The maximum number of invoice items is 250.

```curl
curl https://api.stripe.com/v1/invoiceitems \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "pricing[price]"="{{PRICE_ID}}" \
  -d invoice="{{INVOICE_ID}}"
```

```cli
stripe invoiceitems create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "pricing[price]"="{{PRICE_ID}}" \
  --invoice="{{INVOICE_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice_item = client.v1.invoice_items.create({
  customer: '{{CUSTOMER_ID}}',
  pricing: {price: '{{PRICE_ID}}'},
  invoice: '{{INVOICE_ID}}',
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
  "invoice": "{{INVOICE_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoiceItem = $stripe->invoiceItems->create([
  'customer' => '{{CUSTOMER_ID}}',
  'pricing' => ['price' => '{{PRICE_ID}}'],
  'invoice' => '{{INVOICE_ID}}',
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
    .setInvoice("{{INVOICE_ID}}")
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
  invoice: '{{INVOICE_ID}}',
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
  Invoice: stripe.String("{{INVOICE_ID}}"),
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
    Invoice = "{{INVOICE_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.InvoiceItems;
InvoiceItem invoiceItem = service.Create(options);
```

If you set `auto_advance` to `false`, you can continue to modify the invoice until you [finalize](https://docs.stripe.com/invoicing/integration/workflow-transitions.md) it. To finalize a draft invoice, use the Dashboard, send it to the customer, or pay it. You can also use the [Finalize](https://docs.stripe.com/api/invoices/finalize.md) API:

> If you created the invoice in error, [void](https://docs.stripe.com/invoicing/overview.md#void) it. You can also mark an invoice as [uncollectible](https://docs.stripe.com/invoicing/overview.md#uncollectible).

```curl
curl -X POST https://api.stripe.com/v1/invoices/{{INVOICE_ID}}/finalize \
  -u "<<YOUR_SECRET_KEY>>:"
```

```cli
stripe invoices finalize_invoice {{INVOICE_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.finalize_invoice('{{INVOICE_ID}}')
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.finalize_invoice("{{INVOICE_ID}}")
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->finalizeInvoice('{{INVOICE_ID}}', []);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceFinalizeInvoiceParams params = InvoiceFinalizeInvoiceParams.builder().build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Invoice invoice = client.v1().invoices().finalizeInvoice("{{INVOICE_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const invoice = await stripe.invoices.finalizeInvoice('{{INVOICE_ID}}');
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceFinalizeInvoiceParams{}
result, err := sc.V1Invoices.FinalizeInvoice(
  context.TODO(), "{{INVOICE_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.FinalizeInvoice("{{INVOICE_ID}}");
```

## Accept invoice payment

#### Send an Invoice

Send the invoice to the email address associated with the customer. Stripe finalizes the invoice as soon as you send it. Many jurisdictions consider finalized invoices a legal document making certain fields unalterable. If you send invoices that have already been paid, there’s no reference to the payment in the email.

> When you send invoices that have already been paid, the email doesn’t reference the payment. Stripe sends invoices to the email address associated with the customer.

```curl
curl -X POST https://api.stripe.com/v1/invoices/{{INVOICE_ID}}/send \
  -u "<<YOUR_SECRET_KEY>>:"
```

```cli
stripe invoices send_invoice {{INVOICE_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.send_invoice('{{INVOICE_ID}}')
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.send_invoice("{{INVOICE_ID}}")
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->sendInvoice('{{INVOICE_ID}}', []);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceSendInvoiceParams params = InvoiceSendInvoiceParams.builder().build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Invoice invoice = client.v1().invoices().sendInvoice("{{INVOICE_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const invoice = await stripe.invoices.sendInvoice('{{INVOICE_ID}}');
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceSendInvoiceParams{}
result, err := sc.V1Invoices.SendInvoice(context.TODO(), "{{INVOICE_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.SendInvoice("{{INVOICE_ID}}");
```

#### Stripe Elements

When the invoice is finalized, a *PaymentIntent* (The Payment Intents API tracks the lifecycle of a customer checkout flow and triggers additional authentication steps when required by regulatory mandates, custom Radar fraud rules, or redirect-based payment methods) is generated and associated with the invoice. Use [Stripe Elements](https://docs.stripe.com/payments/elements.md) to collect payment details and confirm the invoice’s PaymentIntent.

> You can’t edit monetary values or the `collection_method` parameter after an invoice is finalized. This restriction also applies to the finalized invoice’s PaymentIntent. When you update the an invoice’s PaymentIntent with an [update](https://docs.stripe.com/api/payment_intents/update.md) call, you can only modify the `setup_future_usage`, `metadata`, `payment_method`, `description`, `receipt_email`, `payment_method_data`, `payment_method_options`, and `shipping` parameters.

#### Payment Element (recommended)

The [Payment Element](https://docs.stripe.com/payments/payment-element.md) securely collects all of the necessary payment details for a variety of payment methods. See [Payment method and product support](https://docs.stripe.com/payments/payment-methods/payment-method-support.md) to determine if your configured payment methods are supported by both Invoicing and the Payment Element.

### Pass the client secret to the front end

Stripe.js uses the PaymentIntent’s [client_secret](https://docs.stripe.com/api/payment_intents/object.md#payment_intent_object-client_secret) to securely complete the payment process. Get the invoice’s client secret by [expanding](https://docs.stripe.com/api/expanding_objects.md) its [confirmation_secret](https://docs.stripe.com/api/invoices/object.md#invoice_object-confirmation_secret) attribute when [finalizing](https://docs.stripe.com/api/invoices/finalize.md) the invoice or when making another API call, such as [retrieve](https://docs.stripe.com/api/invoices/retrieve.md) or [update](https://docs.stripe.com/api/invoices/update.md), on the invoice after you finalize it. Return the `client_secret` to the front end to complete payment.

```curl
curl https://api.stripe.com/v1/invoices/{{INVOICE_ID}}/finalize \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "expand[]"=confirmation_secret
```

```cli
stripe invoices finalize_invoice {{INVOICE_ID}} \
  -d "expand[0]"=confirmation_secret
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.finalize_invoice(
  '{{INVOICE_ID}}',
  {expand: ['confirmation_secret']},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.finalize_invoice(
  "{{INVOICE_ID}}",
  {"expand": ["confirmation_secret"]},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->finalizeInvoice(
  '{{INVOICE_ID}}',
  ['expand' => ['confirmation_secret']]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceFinalizeInvoiceParams params =
  InvoiceFinalizeInvoiceParams.builder().addExpand("confirmation_secret").build();

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
    expand: ['confirmation_secret'],
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceFinalizeInvoiceParams{}
params.AddExpand("confirmation_secret")
result, err := sc.V1Invoices.FinalizeInvoice(
  context.TODO(), "{{INVOICE_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceFinalizeOptions
{
    Expand = new List<string> { "confirmation_secret" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.FinalizeInvoice("{{INVOICE_ID}}", options);
```

After the invoice returns, access the client secret on the expanded `confirmation_secret` field.

#### curl

#### Stripe CLI

#### Ruby

```ruby
client_secret = invoice.confirmation_secret.client_secret
```

#### Python

```python
client_secret = invoice.confirmation_secret.client_secret
```

#### PHP

```php
$client_secret = $invoice->confirmation_secret->client_secret;
```

#### Java

```java
String clientSecret = invoice.getConfirmationSecret().getClientSecret();
```

#### Node.js

```javascript
const client_secret = invoice.confirmation_secret.client_secret;
```

#### Go

```go
clientSecret := invoice.ConfirmationSecret.ClientSecret,
```

#### .NET

```csharp
var clientSecret = invoice.ConfirmationSecret.ClientSecret;
```

### Set up Stripe Elements

The Payment Element is automatically available as a feature of Stripe.js. Include the Stripe.js script on your checkout page by adding it to the `head` of your HTML file. Always load Stripe.js directly from js.stripe.com to remain PCI compliant. Don’t include the script in a bundle or host a copy of it yourself.

```html
<head>
  <title>Pay Invoice</title>
  <script src="https://js.stripe.com/clover/stripe.js"></script>
</head>
```

Create an instance of Stripe with the following JavaScript on your checkout page:

```javascript
// Set your publishable key: remember to change this to your live publishable key in production
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = Stripe('<<YOUR_PUBLISHABLE_KEY>>');
```

### Add the Payment Element to your page

The Payment Element needs a place to live on your payment page. Create an empty DOM node (container) with a unique ID in your payment form.

```html
<form id="payment-form">
  <div id="payment-element">
    <!-- Elements will create form elements here -->
  </div>
  <button id="submit">Pay</button>
  <div id="error-message">
    <!-- Display error message to your customers here -->
  </div>
</form>
```

When the form loads, create an instance of the Payment Element and mount it to the container DOM node. Pass the PaymentIntent’s client secret as an option when creating an instance of Elements.

```javascript
const options = {
  clientSecret: '{{CLIENT_SECRET}}',
  // Fully customizable with appearance API.
  appearance: {/*...*/},
};

// Set up Stripe.js and Elements to use in your checkout form, passing in the client secret.
const elements = stripe.elements(options);

// Create and mount the Payment Element
const paymentElementOptions = { layout: 'accordion'};
const paymentElement = elements.create('payment', paymentElementOptions);
paymentElement.mount('#payment-element');
```

The Payment Element renders a dynamic form that allows your customer to pick a payment method. The form automatically collects all necessary payments details for the payment method selected by the customer.

You can customize the Payment Element to match the design of your site by passing the [appearance object](https://docs.stripe.com/elements/appearance-api.md) into `options` when creating an instance of Elements.

### Complete payment

Use `stripe.confirmPayment` to complete the payment using details from the Payment Element. This creates a payment method and confirms the invoice’s PaymentIntent, causing a charge to be made. If *Strong Customer Authentication* (Strong Customer Authentication (SCA) is a regulatory requirement in effect as of September 14, 2019, that impacts many European online payments. It requires customers to use two-factor authentication like 3D Secure to verify their purchase) (SCA) is required for the payment, the Payment Element handles the authentication process before confirming the PaymentIntent.

Provide a [return_url](https://docs.stripe.com/api/payment_intents/confirm.md#confirm_payment_intent-return_url) to the `confirmPayment` function to indicate where Stripe redirects the user after they complete the payment. Your user might first be redirected to an intermediate site, like a bank authorization page, before being redirected to the `return_url`. Card payments immediately redirect to the `return_url` when a payment is successful.

```javascript
const form = document.getElementById('payment-form');

form.addEventListener('submit', async (event) => {
  event.preventDefault();
const {error} = await stripe.confirmPayment({
    //`Elements` instance that was used to create the Payment Element
    elements,
    confirmParams: {
      return_url: "https://example.com/order/123/complete",
    }
  });

  if (error) {
    // This point will only be reached if there is an immediate error when
    // confirming the payment. Show error to your customer (for example, payment
    // details incomplete)
    const messageContainer = document.querySelector('#error-message');
    messageContainer.textContent = error.message;
  } else {
    // Your customer will be redirected to your `return_url`. For some payment
    // methods like iDEAL, your customer will be redirected to an intermediate
    // site first to authorize the payment, then redirected to the `return_url`.
  }
});
```

When your customer submits a payment, Stripe redirects them to the `return_url` and includes the following URL query parameters. The return page can use them to get the status of the PaymentIntent so it can display the payment status to the customer.

When you specify the `return_url`, you can also append your own query parameters for use on the return page.

| Parameter                      | Description                                                                                                                                                                                                                                                                                                                                                |
| ------------------------------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `payment_intent`               | The unique identifier for the `PaymentIntent`.                                                                                                                                                                                                                                                                                                             |
| `payment_intent_client_secret` | The [client secret](https://docs.stripe.com/api/payment_intents/object.md#payment_intent_object-client_secret) of the `PaymentIntent` object. For subscription integrations, this client_secret is also exposed on the `Invoice` object through [`confirmation_secret`](https://docs.stripe.com/api/invoices/object.md#invoice_object-confirmation_secret) |

When the customer is redirected back to your site, you can use the `payment_intent_client_secret` to query for the PaymentIntent and display the transaction status to your customer.

> If you have tooling that tracks the customer’s browser session, you might need to add the `stripe.com` domain to the referrer exclude list. Redirects cause some tools to create new sessions, which prevents you from tracking the complete session.

Use the `payment_intent_client_secret` query parameter to retrieve the PaymentIntent. Inspect the [status of the PaymentIntent](https://docs.stripe.com/payments/paymentintents/lifecycle.md) to decide what to show your customers. You can also append your own query parameters when providing the `return_url`, which persist through the redirect process.

```javascript
// Initialize Stripe.js using your publishable key
const stripe = Stripe('<<YOUR_PUBLISHABLE_KEY>>');

// Retrieve the "payment_intent_client_secret" query parameter appended to
// your return_url by Stripe.js
const clientSecret = new URLSearchParams(window.location.search).get(
  'payment_intent_client_secret'
);

// Retrieve the PaymentIntent
stripe.retrievePaymentIntent(clientSecret).then(({paymentIntent}) => {
  const message = document.querySelector('#message')

  // Inspect the PaymentIntent `status` to indicate the status of the payment
  // to your customer.
  //
  // Some payment methods will [immediately succeed or fail][0] upon
  // confirmation, while others will first enter a `processing` state.
  //
  // [0]: https://stripe.com/docs/payments/payment-methods#payment-notification
  switch (paymentIntent.status) {
    case 'succeeded':
      message.innerText = 'Success! Payment received.';
      break;

    case 'processing':
      message.innerText = "Payment processing. We'll update you when payment is received.";
      break;

    case 'requires_payment_method':
      message.innerText = 'Payment failed. Please try another payment method.';
      // Redirect your user back to your payment page to attempt collecting
      // payment again
      break;

    default:
      message.innerText = 'Something went wrong.';
      break;
  }
});
```

#### Card Element

The Card Element securely collects and validates credit card details from your user.

### Pass the client secret to the front end

Stripe.js uses the PaymentIntent’s [client_secret](https://docs.stripe.com/api/payment_intents/object.md#payment_intent_object-client_secret) to securely complete the payment process. Get the invoice’s client secret by [expanding](https://docs.stripe.com/api/expanding_objects.md) its [confirmation_secret](https://docs.stripe.com/api/invoices/object.md#invoice_object-confirmation_secret) attribute when [finalizing](https://docs.stripe.com/api/invoices/finalize.md) the invoice or when making another API call, such as [retrieve](https://docs.stripe.com/api/invoices/retrieve.md) or [update](https://docs.stripe.com/api/invoices/update.md), on the invoice after you finalize it. Return the `client_secret` to the front end to complete payment.

```curl
curl https://api.stripe.com/v1/invoices/{{INVOICE_ID}}/finalize \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "expand[]"=confirmation_secret
```

```cli
stripe invoices finalize_invoice {{INVOICE_ID}} \
  -d "expand[0]"=confirmation_secret
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.finalize_invoice(
  '{{INVOICE_ID}}',
  {expand: ['confirmation_secret']},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.finalize_invoice(
  "{{INVOICE_ID}}",
  {"expand": ["confirmation_secret"]},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->finalizeInvoice(
  '{{INVOICE_ID}}',
  ['expand' => ['confirmation_secret']]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceFinalizeInvoiceParams params =
  InvoiceFinalizeInvoiceParams.builder().addExpand("confirmation_secret").build();

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
    expand: ['confirmation_secret'],
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceFinalizeInvoiceParams{}
params.AddExpand("confirmation_secret")
result, err := sc.V1Invoices.FinalizeInvoice(
  context.TODO(), "{{INVOICE_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceFinalizeOptions
{
    Expand = new List<string> { "confirmation_secret" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.FinalizeInvoice("{{INVOICE_ID}}", options);
```

After the invoice returns, access the client secret on the expanded `confirmation_secret` field.

#### curl

#### Stripe CLI

#### Ruby

```ruby
client_secret = invoice.confirmation_secret.client_secret
```

#### Python

```python
client_secret = invoice.confirmation_secret.client_secret
```

#### PHP

```php
$client_secret = $invoice->confirmation_secret->client_secret;
```

#### Java

```java
String clientSecret = invoice.getConfirmationSecret().getClientSecret();
```

#### Node.js

```javascript
const client_secret = invoice.confirmation_secret.client_secret;
```

#### Go

```go
clientSecret := invoice.ConfirmationSecret.ClientSecret,
```

#### .NET

```csharp
var clientSecret = invoice.ConfirmationSecret.ClientSecret;
```

### Set up Stripe Elements

Stripe Elements is included with Stripe.js. Include the Stripe.js script on your checkout page by adding it to the `head` of your HTML file.

Always load Stripe.js directly from js.stripe.com to remain PCI compliant. Don’t include the script in a bundle or host a copy of it yourself.

```html
<head>
  <title>Subscription prices</title>
  <script src="https://js.stripe.com/clover/stripe.js"></script>
</head>
```

Create an instance of Elements with the following JavaScript:

```javascript
// Set your publishable key: remember to change this to your live publishable key in production
// See your keys here: https://dashboard.stripe.com/apikeys
let stripe = Stripe('<<YOUR_PUBLISHABLE_KEY>>');
let elements = stripe.elements();
```

### Add Elements to your page

Elements needs a place to live in your payment form. Create empty DOM nodes (containers) with unique IDs in your payment form and then pass those IDs to Elements.

```html
<body>
  <form id="payment-form">
    <div id="card-element">
      <!-- Elements will create input elements here -->
    </div>

    <!-- We'll put the error messages in this element -->
    <div id="card-element-errors" role="alert"></div>
    <button type="submit">Subscribe</button>
  </form>
</body>
```

Create an instance of an Element and mount it to the Element container:

```javascript
let card = elements.create('card', { style: style });
card.mount('#card-element');
```

The `card` Element simplifies the form and minimizes the number of fields required by inserting a single, flexible input field that securely collects all necessary card details. For a full list of supported Element types, refer to our [Stripe.js reference](https://docs.stripe.com/js.md#elements_create) documentation.

Use the test card number **4242 4242 4242 4242**, any three-digit CVC number, any expiration date in the future, and any five-digit ZIP code.

Elements validates user input as it is typed. To help your customers catch mistakes, listen to `change` events on the `card` Element and display any errors.

```javascript
card.on('change', function (event) {
  displayError(event);
});
function displayError(event) {
  changeLoadingStatePrices(false);
  let displayError = document.getElementById('card-element-errors');
  if (event.error) {
    displayError.textContent = event.error.message;
  } else {
    displayError.textContent = '';
  }
}
```

[ZIP code validation](https://docs.stripe.com/js.md#postal-code-formatting) depends on your customer’s billing country. Use our [international test cards](https://docs.stripe.com/testing.md#international-cards) to experiment with other postal code formats.

### Complete payment

When your customer submits the Elements form, call `stripe.confirmCardPayment` with the *client secret* (The client secret is a unique key returned from Stripe as part of a PaymentIntent. This key lets the client access important fields from the PaymentIntent (status, amount, currency) while hiding sensitive ones (metadata, customer)) that you passed to your frontend. This creates a payment method and *confirms* (Confirming an intent indicates that the customer intends to use the current or provided payment method. Upon confirmation, the intent attempts to initiate the portions of the flow that have real-world side effects) the  invoice’s PaymentIntent causing a charge to be made. If *Strong Customer Authentication* (Strong Customer Authentication (SCA) is a regulatory requirement in effect as of September 14, 2019, that impacts many European online payments. It requires customers to use two-factor authentication like 3D Secure to verify their purchase) (SCA) is required for the payment, Elements handles the authentication process before confirming the PaymentIntent.

```javascript
const btn = document.querySelector('#submit-payment-btn');
btn.addEventListener('click', async (e) => {
  e.preventDefault();
  const nameInput = document.getElementById('name');

  // Create payment method and confirm PaymentIntent.
  stripe.confirmCardPayment(clientSecret, {
    payment_method: {
      card: cardElement,
      billing_details: {
        name: nameInput.value,
      },
    }
  }).then((result) => {
    if(result.error) {
      alert(result.error.message);
    } else {
      // Successful invoice payment
    }
  });
});
```

## Handle post-payment events

Stripe sends an [invoice.paid](https://docs.stripe.com/api/events/types.md?event_types-invoice.paid) event when an invoice payment completes. Listen for this event to ensure reliable fulfillment. If your integration relies on only a client-side callback, the customer could lose connection before the callback executes, which would result in the customer being charged without your server being notified. Setting up your integration to listen for asynchronous events is also what enables you to accept [different types of payment methods](https://stripe.com/payments/payment-methods-guide) with a single integration.

> Successful invoice payments trigger both an [invoice.paid](https://docs.stripe.com/api/events/types.md?event_types-invoice.paid) and [invoice.payment_succeeded](https://docs.stripe.com/api/events/types.md?event_types-invoice.payment_succeeded) event. Both event types contain the same invoice data, so it’s only necessary to listen to one of them to be notified of successful invoice payments. The difference is that `invoice.payment_succeeded` events are sent for successful invoice payments, but aren’t sent when you mark an invoice as [paid_out_of_band](https://docs.stripe.com/api/invoices/pay.md#pay_invoice-paid_out_of_band). `invoice.paid` events, on the other hand, are triggered for both successful payments and out of band payments. Because `invoice.paid` covers both scenarios, we typically recommend listening to `invoice.paid` rather than `invoice.payment_succeeded`.

Use the [Dashboard webhook tool](https://dashboard.stripe.com/webhooks) or follow the [webhook quickstart](https://docs.stripe.com/webhooks/quickstart.md) to receive these events and run actions, such as sending an order confirmation email to your customer, logging the sale in a database, or starting a shipping workflow.

In addition to handling the `invoice.paid` event, we recommend handling two other events when collecting payments with the Payment Element:

| Event                                                                                                                    | Description                                                                                                                                                                                                                                            | Action                                                                                                                                                                          |
| ------------------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| [payment_intent.processing](https://docs.stripe.com/api/events/types.md?lang=php#event_types-payment_intent.processing)  | Sent when a customer successfully initiated a payment, but the payment has yet to complete. This event is most commonly sent when a bank debit is initiated. It’s followed by either a `invoice.paid` or `invoice.payment_failed` event in the future. | Send the customer an order confirmation that indicates their payment is pending. For digital goods, you might want to fulfill the order before waiting for payment to complete. |
| [invoice.payment_failed](https://docs.stripe.com/api/events/types.md?lang=php#event_types-payment_intent.payment_failed) | Sent when a customer attempted a payment on an invoice, but the payment failed.                                                                                                                                                                        | If a payment transitioned from `processing` to `payment_failed`, offer the customer another attempt to pay.                                                                     |

## Optional: Customize an invoice

You can [customize invoices](https://docs.stripe.com/invoicing/customize.md) in several ways. Stripe’s customization options allow you to add your own branding and to modify your invoices so that they comply in the jurisdictions ​​where you operate.

### Custom fields 

Add custom fields to enhance your invoice PDF documents and help you comply with your business practice and tax reporting obligations. Custom fields allow you to provide up to four key-value pairs ​​that display in the invoice header. You can set up to four custom field key-value pairs in the [Invoice Editor](https://dashboard.stripe.com/invoices/create), with the [Invoices API](https://docs.stripe.com/api/invoices/create.md#create_invoice-custom_fields), or with [Invoice Templates](https://docs.stripe.com/invoicing/invoice-rendering-template.md).

Some common uses for custom fields are:

- Purchase Order (PO) numbers
- Contractor numbers
- Tax compliance

Here’s an example of creating an invoice with a PO Number and value-added tax (VAT) as custom fields:

```curl
curl https://api.stripe.com/v1/invoices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "custom_fields[0][name]"="PO number" \
  -d "custom_fields[0][value]"=12345 \
  -d "custom_fields[1][name]"=VAT \
  -d "custom_fields[1][value]"=123ABC
```

```cli
stripe invoices create  \
  --customer="{{CUSTOMER_ID}}" \
  -d "custom_fields[0][name]"="PO number" \
  -d "custom_fields[0][value]"=12345 \
  -d "custom_fields[1][name]"=VAT \
  -d "custom_fields[1][value]"=123ABC
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.create({
  customer: '{{CUSTOMER_ID}}',
  custom_fields: [
    {
      name: 'PO number',
      value: '12345',
    },
    {
      name: 'VAT',
      value: '123ABC',
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice = client.v1.invoices.create({
  "customer": "{{CUSTOMER_ID}}",
  "custom_fields": [
    {"name": "PO number", "value": "12345"},
    {"name": "VAT", "value": "123ABC"},
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoice = $stripe->invoices->create([
  'customer' => '{{CUSTOMER_ID}}',
  'custom_fields' => [
    [
      'name' => 'PO number',
      'value' => '12345',
    ],
    [
      'name' => 'VAT',
      'value' => '123ABC',
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceCreateParams params =
  InvoiceCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .addCustomField(
      InvoiceCreateParams.CustomField.builder()
        .setName("PO number")
        .setValue("12345")
        .build()
    )
    .addCustomField(
      InvoiceCreateParams.CustomField.builder().setName("VAT").setValue("123ABC").build()
    )
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
  custom_fields: [
    {
      name: 'PO number',
      value: '12345',
    },
    {
      name: 'VAT',
      value: '123ABC',
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  CustomFields: []*stripe.InvoiceCreateCustomFieldParams{
    &stripe.InvoiceCreateCustomFieldParams{
      Name: stripe.String("PO number"),
      Value: stripe.String("12345"),
    },
    &stripe.InvoiceCreateCustomFieldParams{
      Name: stripe.String("VAT"),
      Value: stripe.String("123ABC"),
    },
  },
}
result, err := sc.V1Invoices.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    CustomFields = new List<InvoiceCustomFieldOptions>
    {
        new InvoiceCustomFieldOptions { Name = "PO number", Value = "12345" },
        new InvoiceCustomFieldOptions { Name = "VAT", Value = "123ABC" },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.Create(options);
```

#### Custom field inheritance 

You can set custom invoice fields on the [Customer](https://docs.stripe.com/api/customers/object.md#customer_object-invoice_settings-custom_fields) object. Any custom fields you set at the customer level apply to all of the draft invoices you generate for that customer. You can always modify these inherited custom fields while the invoice is still a draft. After the invoice finalizes, you can’t update the custom fields.

Here’s an example of adding custom fields at the customer level to apply to all future generated draft invoices:

```curl
curl https://api.stripe.com/v1/customers \
  -u "<<YOUR_SECRET_KEY>>:" \
  --data-urlencode email="jenny.rosen@example.com" \
  -d payment_method=pm_card_visa \
  -d "invoice_settings[default_payment_method]"=pm_card_visa \
  -d "invoice_settings[custom_fields][0][name]"="PO number" \
  -d "invoice_settings[custom_fields][0][value]"=12345 \
  -d "invoice_settings[custom_fields][1][name]"=VAT \
  -d "invoice_settings[custom_fields][1][value]"=123ABC
```

```cli
stripe customers create  \
  --email="jenny.rosen@example.com" \
  --payment-method=pm_card_visa \
  -d "invoice_settings[default_payment_method]"=pm_card_visa \
  -d "invoice_settings[custom_fields][0][name]"="PO number" \
  -d "invoice_settings[custom_fields][0][value]"=12345 \
  -d "invoice_settings[custom_fields][1][name]"=VAT \
  -d "invoice_settings[custom_fields][1][value]"=123ABC
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

customer = client.v1.customers.create({
  email: 'jenny.rosen@example.com',
  payment_method: 'pm_card_visa',
  invoice_settings: {
    default_payment_method: 'pm_card_visa',
    custom_fields: [
      {
        name: 'PO number',
        value: '12345',
      },
      {
        name: 'VAT',
        value: '123ABC',
      },
    ],
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
customer = client.v1.customers.create({
  "email": "jenny.rosen@example.com",
  "payment_method": "pm_card_visa",
  "invoice_settings": {
    "default_payment_method": "pm_card_visa",
    "custom_fields": [
      {"name": "PO number", "value": "12345"},
      {"name": "VAT", "value": "123ABC"},
    ],
  },
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$customer = $stripe->customers->create([
  'email' => 'jenny.rosen@example.com',
  'payment_method' => 'pm_card_visa',
  'invoice_settings' => [
    'default_payment_method' => 'pm_card_visa',
    'custom_fields' => [
      [
        'name' => 'PO number',
        'value' => '12345',
      ],
      [
        'name' => 'VAT',
        'value' => '123ABC',
      ],
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CustomerCreateParams params =
  CustomerCreateParams.builder()
    .setEmail("jenny.rosen@example.com")
    .setPaymentMethod("pm_card_visa")
    .setInvoiceSettings(
      CustomerCreateParams.InvoiceSettings.builder()
        .setDefaultPaymentMethod("pm_card_visa")
        .addCustomField(
          CustomerCreateParams.InvoiceSettings.CustomField.builder()
            .setName("PO number")
            .setValue("12345")
            .build()
        )
        .addCustomField(
          CustomerCreateParams.InvoiceSettings.CustomField.builder()
            .setName("VAT")
            .setValue("123ABC")
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Customer customer = client.v1().customers().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const customer = await stripe.customers.create({
  email: 'jenny.rosen@example.com',
  payment_method: 'pm_card_visa',
  invoice_settings: {
    default_payment_method: 'pm_card_visa',
    custom_fields: [
      {
        name: 'PO number',
        value: '12345',
      },
      {
        name: 'VAT',
        value: '123ABC',
      },
    ],
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CustomerCreateParams{
  Email: stripe.String("jenny.rosen@example.com"),
  PaymentMethod: stripe.String("pm_card_visa"),
  InvoiceSettings: &stripe.CustomerCreateInvoiceSettingsParams{
    DefaultPaymentMethod: stripe.String("pm_card_visa"),
    CustomFields: []*stripe.CustomerCreateInvoiceSettingsCustomFieldParams{
      &stripe.CustomerCreateInvoiceSettingsCustomFieldParams{
        Name: stripe.String("PO number"),
        Value: stripe.String("12345"),
      },
      &stripe.CustomerCreateInvoiceSettingsCustomFieldParams{
        Name: stripe.String("VAT"),
        Value: stripe.String("123ABC"),
      },
    },
  },
}
result, err := sc.V1Customers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CustomerCreateOptions
{
    Email = "jenny.rosen@example.com",
    PaymentMethod = "pm_card_visa",
    InvoiceSettings = new CustomerInvoiceSettingsOptions
    {
        DefaultPaymentMethod = "pm_card_visa",
        CustomFields = new List<CustomerInvoiceSettingsCustomFieldOptions>
        {
            new CustomerInvoiceSettingsCustomFieldOptions
            {
                Name = "PO number",
                Value = "12345",
            },
            new CustomerInvoiceSettingsCustomFieldOptions
            {
                Name = "VAT",
                Value = "123ABC",
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Customers;
Customer customer = service.Create(options);
```

## See also

- [Post-finalization](https://docs.stripe.com/invoicing/integration/workflow-transitions.md#post-finalized)
- [Use incoming webhooks to get real-time updates](https://docs.stripe.com/webhooks.md)
