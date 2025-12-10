# Multi-currency customers

Change the billable currency for any customer to accept multiple currencies.

If you need to sell in multiple currencies but have each individual customer use a single currency, see [multi-currency Prices](https://docs.stripe.com/products-prices/how-products-and-prices-work.md#multiple-currencies).

Use the Invoicing API to issue an invoice to a customer in a different currency. With the multi-currency customers feature, you can bill the same *Customer* (Customer objects represent customers of your business. They let you reuse payment methods and give you the ability to track multiple payments) using a different currency than what’s set as their default currency, and change the currency for a customer’s subscriptions. You can’t have two active subscriptions with different currencies.

This guide also explains how to create a credit note and inspect a customer’s credit balance in all assigned currencies. For illustrative purposes, we use the Canadian Dollar (CAD).

## Create an invoice 

Before you invoice a customer, create an invoice item by passing in the customer `id`, `amount`, and `currency`. Only add invoice items to a single customer at a time to avoid adding them to the wrong one.

The maximum number of invoice items is 250. Creating an invoice adds up to 250 pending invoice items with the remainder to be added on the next invoice. To see your customer’s pending invoice items, see the **Customer details** page or set the [pending](https://docs.stripe.com/api/invoiceitems/list.md#list_invoiceitems-pending) attribute to `true` when you use the API to list all of the invoice items.

> A CAD invoice doesn’t apply a customer credit balance denominated in USD or any other currency other than CAD. Additionally, any amount-off coupons you applied to the customer that are denominated in non-CAD currency are ignored.

```curl
curl https://api.stripe.com/v1/invoiceitems \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d amount=1000 \
  -d currency=cad
```

```cli
stripe invoiceitems create  \
  --customer="{{CUSTOMER_ID}}" \
  --amount=1000 \
  --currency=cad
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice_item = client.v1.invoice_items.create({
  customer: '{{CUSTOMER_ID}}',
  amount: 1000,
  currency: 'cad',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
invoice_item = client.v1.invoice_items.create({
  "customer": "{{CUSTOMER_ID}}",
  "amount": 1000,
  "currency": "cad",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$invoiceItem = $stripe->invoiceItems->create([
  'customer' => '{{CUSTOMER_ID}}',
  'amount' => 1000,
  'currency' => 'cad',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InvoiceItemCreateParams params =
  InvoiceItemCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .setAmount(1000L)
    .setCurrency("cad")
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
  amount: 1000,
  currency: 'cad',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.InvoiceItemCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  Amount: stripe.Int64(1000),
  Currency: stripe.String(stripe.CurrencyCAD),
}
result, err := sc.V1InvoiceItems.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new InvoiceItemCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    Amount = 1000,
    Currency = "cad",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.InvoiceItems;
InvoiceItem invoiceItem = service.Create(options);
```

You must pass in the `currency` parameter when you issue a multi-currency invoice. The `currency` parameter dictates which invoice items get pulled into the invoice. For example, if you were to create two invoice items—one in USD and the other in CAD—for the same customer, setting the currency to CAD would only pull in the CAD invoice item (ignoring the USD invoice item).

```curl
curl https://api.stripe.com/v1/invoices \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d customer="{{CUSTOMER_ID}}" \
  -d collection_method=send_invoice \
  -d days_until_due=30 \
  -d pending_invoice_items_behavior=include \
  -d currency=cad
```

```cli
stripe invoices create  \
  --customer="{{CUSTOMER_ID}}" \
  --collection-method=send_invoice \
  --days-until-due=30 \
  --pending-invoice-items-behavior=include \
  --currency=cad
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

invoice = client.v1.invoices.create({
  customer: '{{CUSTOMER_ID}}',
  collection_method: 'send_invoice',
  days_until_due: 30,
  pending_invoice_items_behavior: 'include',
  currency: 'cad',
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
  "pending_invoice_items_behavior": "include",
  "currency": "cad",
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
  'pending_invoice_items_behavior' => 'include',
  'currency' => 'cad',
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
    .setPendingInvoiceItemsBehavior(
      InvoiceCreateParams.PendingInvoiceItemsBehavior.INCLUDE
    )
    .setCurrency("cad")
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
  pending_invoice_items_behavior: 'include',
  currency: 'cad',
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
  PendingInvoiceItemsBehavior: stripe.String("include"),
  Currency: stripe.String(stripe.CurrencyCAD),
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
    PendingInvoiceItemsBehavior = "include",
    Currency = "cad",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Invoices;
Invoice invoice = service.Create(options);
```

## Create a credit note 

If there’s an issue with the invoice, you can create a credit note. If you need to apply the credit to the customer’s credit balance (as opposed to back to the original payment method), Stripe allocates the credit amount to the CAD-specific credit balance.

```curl
curl https://api.stripe.com/v1/credit_notes \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d invoice={{INVOICE_ID}} \
  -d reason=duplicate \
  -d amount=1000 \
  -d credit_amount=1000
```

```cli
stripe credit_notes create  \
  --invoice={{INVOICE_ID}} \
  --reason=duplicate \
  --amount=1000 \
  --credit-amount=1000
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

credit_note = client.v1.credit_notes.create({
  invoice: '{{INVOICE_ID}}',
  reason: 'duplicate',
  amount: 1000,
  credit_amount: 1000,
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
credit_note = client.v1.credit_notes.create({
  "invoice": "{{INVOICE_ID}}",
  "reason": "duplicate",
  "amount": 1000,
  "credit_amount": 1000,
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$creditNote = $stripe->creditNotes->create([
  'invoice' => '{{INVOICE_ID}}',
  'reason' => 'duplicate',
  'amount' => 1000,
  'credit_amount' => 1000,
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CreditNoteCreateParams params =
  CreditNoteCreateParams.builder()
    .setInvoice("{{INVOICE_ID}}")
    .setReason(CreditNoteCreateParams.Reason.DUPLICATE)
    .setAmount(1000L)
    .setCreditAmount(1000L)
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
CreditNote creditNote = client.v1().creditNotes().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const creditNote = await stripe.creditNotes.create({
  invoice: '{{INVOICE_ID}}',
  reason: 'duplicate',
  amount: 1000,
  credit_amount: 1000,
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CreditNoteCreateParams{
  Invoice: stripe.String("{{INVOICE_ID}}"),
  Reason: stripe.String(stripe.CreditNoteReasonDuplicate),
  Amount: stripe.Int64(1000),
  CreditAmount: stripe.Int64(1000),
}
result, err := sc.V1CreditNotes.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CreditNoteCreateOptions
{
    Invoice = "{{INVOICE_ID}}",
    Reason = "duplicate",
    Amount = 1000,
    CreditAmount = 1000,
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.CreditNotes;
CreditNote creditNote = service.Create(options);
```

## Inspect the credit balance 

To see how much credit a customer has in each currency, use the `invoice_credit_balance` parameter:

```curl
curl -G https://api.stripe.com/v1/customers/{{CUSTOMER_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "expand[]"=invoice_credit_balance
```

```cli
stripe customers retrieve {{CUSTOMER_ID}} \
  -d "expand[0]"=invoice_credit_balance
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

customer = client.v1.customers.retrieve(
  '{{CUSTOMER_ID}}',
  {expand: ['invoice_credit_balance']},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
customer = client.v1.customers.retrieve(
  "{{CUSTOMER_ID}}",
  {"expand": ["invoice_credit_balance"]},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$customer = $stripe->customers->retrieve(
  '{{CUSTOMER_ID}}',
  ['expand' => ['invoice_credit_balance']]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CustomerRetrieveParams params =
  CustomerRetrieveParams.builder().addExpand("invoice_credit_balance").build();

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
    expand: ['invoice_credit_balance'],
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CustomerRetrieveParams{}
params.AddExpand("invoice_credit_balance")
result, err := sc.V1Customers.Retrieve(context.TODO(), "{{CUSTOMER_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CustomerGetOptions
{
    Expand = new List<string> { "invoice_credit_balance" },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Customers;
Customer customer = service.Get("{{CUSTOMER_ID}}", options);
```

The customer’s credit balance is drawn down from the next CAD invoice created for this customer. It won’t, however, be drawn down for invoices created in different currencies.

## See also

- [Integrate with the Invoicing API](https://docs.stripe.com/invoicing/integration.md)
- [Manage customers](https://docs.stripe.com/invoicing/customer.md)
- [Products and prices](https://docs.stripe.com/invoicing/products-prices.md)
