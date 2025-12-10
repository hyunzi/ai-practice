# Generate credit notes programmatically

Use the Invoicing API to adjust or refund finalized invoices with credit notes.

To adjust the balance of an `open` or `paid` *invoice* (Invoices are statements of amounts owed by a customer. They track the status of payments from draft through paid or otherwise finalized. Subscriptions automatically generate invoices, or you can manually create a one-off invoice), generate a [credit note](https://docs.stripe.com/api/credit_notes.md).

> For information about working with credit notes using the Dashboard, see [Issue credit notes](https://docs.stripe.com/invoicing/dashboard/credit-notes.md).

When you create a credit note, you can apply credit amounts in three ways:

- Discount a fixed amount from an invoice line item.
- Discount a quantity from an invoice line item. The total discount is the discount quantity times the unit price of that line item.
- Apply a discount to the total invoice amount by adding a custom discount line item with a description, quantity, and unit price. The total discount is the quantity times the unit price.

We recommend discounting invoice line items when possible, since it associates each credit with a line item. Adding a custom discount line item can make reporting and tracking difficult, because the credit isn’t associated with a real invoice line item.

> You can’t combine discount types on an invoice line item. For example, if you discount a line item quantity, then a future credit note can only discount that line item by quantity, not by amount. If you discount a line item amount, then a future credit note can only discount that line item by amount, not by quantity.

## Credit notes for open invoices 

When you create a custom line item on a credit note for an `open` invoice, the `amount_due` on the invoice decreases based on the `custom_line_items` in the credit note. This is in addition to any adjustments you make to existing `invoice_line_items`. For example, if the amount due on an `open` invoice is 100 USD and you create a `custom_line_item` with `quantity=1` and `unit_amount=2000`, the new amount due on the invoice is 80 USD.

On an invoice, credit notes appear as items after applying discounts and taxes, making them a post-tax adjusted amount. We calculate the invoice amount due using the following order:

1. Sum of invoice line items
1. Discounts
1. Pre-tax invoice total
1. Taxes
1. Customer credit balance
1. Credit notes applied (to gross amount due)
1. New amount due

If applying a credit note to an invoice changes the amount due to zero, the invoice automatically transitions into the `paid` state. If you want to change the quantity or amount of an existing line item, pass the invoice ID, the line item ID, and the new quantity or amount. When a line item has a quantity and an amount, you can only update the quantity. Otherwise, you can only change the amount. The example below adjusts the quantity to two:

```curl
curl https://api.stripe.com/v1/credit_notes \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d invoice={{INVOICE_ID}} \
  -d "lines[0][type]"=invoice_line_item \
  -d "lines[0][invoice_line_item]"={{INVOICE_LINE_ITEM}} \
  -d "lines[0][quantity]"=2
```

```cli
stripe credit_notes create  \
  --invoice={{INVOICE_ID}} \
  -d "lines[0][type]"=invoice_line_item \
  -d "lines[0][invoice_line_item]"={{INVOICE_LINE_ITEM}} \
  -d "lines[0][quantity]"=2
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

credit_note = client.v1.credit_notes.create({
  invoice: '{{INVOICE_ID}}',
  lines: [
    {
      type: 'invoice_line_item',
      invoice_line_item: '{{INVOICE_LINE_ITEM}}',
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
credit_note = client.v1.credit_notes.create({
  "invoice": "{{INVOICE_ID}}",
  "lines": [
    {
      "type": "invoice_line_item",
      "invoice_line_item": "{{INVOICE_LINE_ITEM}}",
      "quantity": 2,
    },
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$creditNote = $stripe->creditNotes->create([
  'invoice' => '{{INVOICE_ID}}',
  'lines' => [
    [
      'type' => 'invoice_line_item',
      'invoice_line_item' => '{{INVOICE_LINE_ITEM}}',
      'quantity' => 2,
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CreditNoteCreateParams params =
  CreditNoteCreateParams.builder()
    .setInvoice("{{INVOICE_ID}}")
    .addLine(
      CreditNoteCreateParams.Line.builder()
        .setType(CreditNoteCreateParams.Line.Type.INVOICE_LINE_ITEM)
        .setInvoiceLineItem("{{INVOICE_LINE_ITEM}}")
        .setQuantity(2L)
        .build()
    )
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
  lines: [
    {
      type: 'invoice_line_item',
      invoice_line_item: '{{INVOICE_LINE_ITEM}}',
      quantity: 2,
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CreditNoteCreateParams{
  Invoice: stripe.String("{{INVOICE_ID}}"),
  Lines: []*stripe.CreditNoteCreateLineParams{
    &stripe.CreditNoteCreateLineParams{
      Type: stripe.String("invoice_line_item"),
      InvoiceLineItem: stripe.String("{{INVOICE_LINE_ITEM}}"),
      Quantity: stripe.Int64(2),
    },
  },
}
result, err := sc.V1CreditNotes.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CreditNoteCreateOptions
{
    Invoice = "{{INVOICE_ID}}",
    Lines = new List<CreditNoteLineOptions>
    {
        new CreditNoteLineOptions
        {
            Type = "invoice_line_item",
            InvoiceLineItem = "{{INVOICE_LINE_ITEM}}",
            Quantity = 2,
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.CreditNotes;
CreditNote creditNote = service.Create(options);
```

To create a custom line item on the credit note, pass the invoice ID, description, quantity, and unit amount. You can (optionally) set a tax rate as well. This example creates a custom line item on the credit note for 10 USD:

```curl
curl https://api.stripe.com/v1/credit_notes \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d invoice={{INVOICE_ID}} \
  -d "lines[0][type]"=custom_line_item \
  -d "lines[0][description]"="Courtesy credit" \
  -d "lines[0][quantity]"=1 \
  -d "lines[0][unit_amount]"=1000
```

```cli
stripe credit_notes create  \
  --invoice={{INVOICE_ID}} \
  -d "lines[0][type]"=custom_line_item \
  -d "lines[0][description]"="Courtesy credit" \
  -d "lines[0][quantity]"=1 \
  -d "lines[0][unit_amount]"=1000
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

credit_note = client.v1.credit_notes.create({
  invoice: '{{INVOICE_ID}}',
  lines: [
    {
      type: 'custom_line_item',
      description: 'Courtesy credit',
      quantity: 1,
      unit_amount: 1000,
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
credit_note = client.v1.credit_notes.create({
  "invoice": "{{INVOICE_ID}}",
  "lines": [
    {
      "type": "custom_line_item",
      "description": "Courtesy credit",
      "quantity": 1,
      "unit_amount": 1000,
    },
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$creditNote = $stripe->creditNotes->create([
  'invoice' => '{{INVOICE_ID}}',
  'lines' => [
    [
      'type' => 'custom_line_item',
      'description' => 'Courtesy credit',
      'quantity' => 1,
      'unit_amount' => 1000,
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CreditNoteCreateParams params =
  CreditNoteCreateParams.builder()
    .setInvoice("{{INVOICE_ID}}")
    .addLine(
      CreditNoteCreateParams.Line.builder()
        .setType(CreditNoteCreateParams.Line.Type.CUSTOM_LINE_ITEM)
        .setDescription("Courtesy credit")
        .setQuantity(1L)
        .setUnitAmount(1000L)
        .build()
    )
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
  lines: [
    {
      type: 'custom_line_item',
      description: 'Courtesy credit',
      quantity: 1,
      unit_amount: 1000,
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CreditNoteCreateParams{
  Invoice: stripe.String("{{INVOICE_ID}}"),
  Lines: []*stripe.CreditNoteCreateLineParams{
    &stripe.CreditNoteCreateLineParams{
      Type: stripe.String("custom_line_item"),
      Description: stripe.String("Courtesy credit"),
      Quantity: stripe.Int64(1),
      UnitAmount: stripe.Int64(1000),
    },
  },
}
result, err := sc.V1CreditNotes.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CreditNoteCreateOptions
{
    Invoice = "{{INVOICE_ID}}",
    Lines = new List<CreditNoteLineOptions>
    {
        new CreditNoteLineOptions
        {
            Type = "custom_line_item",
            Description = "Courtesy credit",
            Quantity = 1,
            UnitAmount = 1000,
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.CreditNotes;
CreditNote creditNote = service.Create(options);
```

## Credit notes for paid invoices 

When you create a credit note for a `paid` invoice, the amount due on the invoice doesn’t change. Instead, the user can choose one or more of the following options:

| Action                             | Description                                                                                  |
| ---------------------------------- | -------------------------------------------------------------------------------------------- |
| Create a refund                    | Refund the invoice’s charge back to the customer’s payment method.                           |
| Link a refund                      | Link an existing refund for the invoice’s charge.                                            |
| Credit the customer credit balance | Credit the customer credit balance, which is automatically applied to their future invoices. |
| Credit outside of Stripe           | Credit the invoice for an amount made in an adjustment outside of Stripe.                    |

In the following example, the credit note creates a refund:

```curl
curl https://api.stripe.com/v1/credit_notes \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d invoice={{INVOICE_ID}} \
  -d "lines[0][type]"=invoice_line_item \
  -d "lines[0][invoice_line_item]"={{INVOICE_LINE_ITEM}} \
  -d "lines[0][quantity]"=2 \
  -d refund_amount=500
```

```cli
stripe credit_notes create  \
  --invoice={{INVOICE_ID}} \
  -d "lines[0][type]"=invoice_line_item \
  -d "lines[0][invoice_line_item]"={{INVOICE_LINE_ITEM}} \
  -d "lines[0][quantity]"=2 \
  --refund-amount=500
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

credit_note = client.v1.credit_notes.create({
  invoice: '{{INVOICE_ID}}',
  lines: [
    {
      type: 'invoice_line_item',
      invoice_line_item: '{{INVOICE_LINE_ITEM}}',
      quantity: 2,
    },
  ],
  refund_amount: 500,
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
credit_note = client.v1.credit_notes.create({
  "invoice": "{{INVOICE_ID}}",
  "lines": [
    {
      "type": "invoice_line_item",
      "invoice_line_item": "{{INVOICE_LINE_ITEM}}",
      "quantity": 2,
    },
  ],
  "refund_amount": 500,
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$creditNote = $stripe->creditNotes->create([
  'invoice' => '{{INVOICE_ID}}',
  'lines' => [
    [
      'type' => 'invoice_line_item',
      'invoice_line_item' => '{{INVOICE_LINE_ITEM}}',
      'quantity' => 2,
    ],
  ],
  'refund_amount' => 500,
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CreditNoteCreateParams params =
  CreditNoteCreateParams.builder()
    .setInvoice("{{INVOICE_ID}}")
    .addLine(
      CreditNoteCreateParams.Line.builder()
        .setType(CreditNoteCreateParams.Line.Type.INVOICE_LINE_ITEM)
        .setInvoiceLineItem("{{INVOICE_LINE_ITEM}}")
        .setQuantity(2L)
        .build()
    )
    .setRefundAmount(500L)
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
  lines: [
    {
      type: 'invoice_line_item',
      invoice_line_item: '{{INVOICE_LINE_ITEM}}',
      quantity: 2,
    },
  ],
  refund_amount: 500,
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CreditNoteCreateParams{
  Invoice: stripe.String("{{INVOICE_ID}}"),
  Lines: []*stripe.CreditNoteCreateLineParams{
    &stripe.CreditNoteCreateLineParams{
      Type: stripe.String("invoice_line_item"),
      InvoiceLineItem: stripe.String("{{INVOICE_LINE_ITEM}}"),
      Quantity: stripe.Int64(2),
    },
  },
  RefundAmount: stripe.Int64(500),
}
result, err := sc.V1CreditNotes.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CreditNoteCreateOptions
{
    Invoice = "{{INVOICE_ID}}",
    Lines = new List<CreditNoteLineOptions>
    {
        new CreditNoteLineOptions
        {
            Type = "invoice_line_item",
            InvoiceLineItem = "{{INVOICE_LINE_ITEM}}",
            Quantity = 2,
        },
    },
    RefundAmount = 500,
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.CreditNotes;
CreditNote creditNote = service.Create(options);
```

Here, the credit note creates a credit to the customer credit balance:

```curl
curl https://api.stripe.com/v1/credit_notes \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d invoice={{INVOICE_ID}} \
  -d "lines[0][type]"=invoice_line_item \
  -d "lines[0][invoice_line_item]"={{INVOICE_LINE_ITEM}} \
  -d "lines[0][quantity]"=2 \
  -d credit_amount=500
```

```cli
stripe credit_notes create  \
  --invoice={{INVOICE_ID}} \
  -d "lines[0][type]"=invoice_line_item \
  -d "lines[0][invoice_line_item]"={{INVOICE_LINE_ITEM}} \
  -d "lines[0][quantity]"=2 \
  --credit-amount=500
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

credit_note = client.v1.credit_notes.create({
  invoice: '{{INVOICE_ID}}',
  lines: [
    {
      type: 'invoice_line_item',
      invoice_line_item: '{{INVOICE_LINE_ITEM}}',
      quantity: 2,
    },
  ],
  credit_amount: 500,
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
credit_note = client.v1.credit_notes.create({
  "invoice": "{{INVOICE_ID}}",
  "lines": [
    {
      "type": "invoice_line_item",
      "invoice_line_item": "{{INVOICE_LINE_ITEM}}",
      "quantity": 2,
    },
  ],
  "credit_amount": 500,
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$creditNote = $stripe->creditNotes->create([
  'invoice' => '{{INVOICE_ID}}',
  'lines' => [
    [
      'type' => 'invoice_line_item',
      'invoice_line_item' => '{{INVOICE_LINE_ITEM}}',
      'quantity' => 2,
    ],
  ],
  'credit_amount' => 500,
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CreditNoteCreateParams params =
  CreditNoteCreateParams.builder()
    .setInvoice("{{INVOICE_ID}}")
    .addLine(
      CreditNoteCreateParams.Line.builder()
        .setType(CreditNoteCreateParams.Line.Type.INVOICE_LINE_ITEM)
        .setInvoiceLineItem("{{INVOICE_LINE_ITEM}}")
        .setQuantity(2L)
        .build()
    )
    .setCreditAmount(500L)
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
  lines: [
    {
      type: 'invoice_line_item',
      invoice_line_item: '{{INVOICE_LINE_ITEM}}',
      quantity: 2,
    },
  ],
  credit_amount: 500,
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CreditNoteCreateParams{
  Invoice: stripe.String("{{INVOICE_ID}}"),
  Lines: []*stripe.CreditNoteCreateLineParams{
    &stripe.CreditNoteCreateLineParams{
      Type: stripe.String("invoice_line_item"),
      InvoiceLineItem: stripe.String("{{INVOICE_LINE_ITEM}}"),
      Quantity: stripe.Int64(2),
    },
  },
  CreditAmount: stripe.Int64(500),
}
result, err := sc.V1CreditNotes.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CreditNoteCreateOptions
{
    Invoice = "{{INVOICE_ID}}",
    Lines = new List<CreditNoteLineOptions>
    {
        new CreditNoteLineOptions
        {
            Type = "invoice_line_item",
            InvoiceLineItem = "{{INVOICE_LINE_ITEM}}",
            Quantity = 2,
        },
    },
    CreditAmount = 500,
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.CreditNotes;
CreditNote creditNote = service.Create(options);
```

You can also combine multiple parameters. Funds that are left over after subtracting the refund and credit amounts from the invoice `amount` results in a credit outside of Stripe, usually with cash or a check:

```curl
curl https://api.stripe.com/v1/credit_notes \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d invoice={{INVOICE_ID}} \
  -d "lines[0][type]"=invoice_line_item \
  -d "lines[0][invoice_line_item]"={{INVOICE_LINE_ITEM}} \
  -d "lines[0][quantity]"=2 \
  -d refund_amount=100 \
  -d credit_amount=200 \
  -d out_of_band_amount=200
```

```cli
stripe credit_notes create  \
  --invoice={{INVOICE_ID}} \
  -d "lines[0][type]"=invoice_line_item \
  -d "lines[0][invoice_line_item]"={{INVOICE_LINE_ITEM}} \
  -d "lines[0][quantity]"=2 \
  --refund-amount=100 \
  --credit-amount=200 \
  --out-of-band-amount=200
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

credit_note = client.v1.credit_notes.create({
  invoice: '{{INVOICE_ID}}',
  lines: [
    {
      type: 'invoice_line_item',
      invoice_line_item: '{{INVOICE_LINE_ITEM}}',
      quantity: 2,
    },
  ],
  refund_amount: 100,
  credit_amount: 200,
  out_of_band_amount: 200,
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
credit_note = client.v1.credit_notes.create({
  "invoice": "{{INVOICE_ID}}",
  "lines": [
    {
      "type": "invoice_line_item",
      "invoice_line_item": "{{INVOICE_LINE_ITEM}}",
      "quantity": 2,
    },
  ],
  "refund_amount": 100,
  "credit_amount": 200,
  "out_of_band_amount": 200,
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$creditNote = $stripe->creditNotes->create([
  'invoice' => '{{INVOICE_ID}}',
  'lines' => [
    [
      'type' => 'invoice_line_item',
      'invoice_line_item' => '{{INVOICE_LINE_ITEM}}',
      'quantity' => 2,
    ],
  ],
  'refund_amount' => 100,
  'credit_amount' => 200,
  'out_of_band_amount' => 200,
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CreditNoteCreateParams params =
  CreditNoteCreateParams.builder()
    .setInvoice("{{INVOICE_ID}}")
    .addLine(
      CreditNoteCreateParams.Line.builder()
        .setType(CreditNoteCreateParams.Line.Type.INVOICE_LINE_ITEM)
        .setInvoiceLineItem("{{INVOICE_LINE_ITEM}}")
        .setQuantity(2L)
        .build()
    )
    .setRefundAmount(100L)
    .setCreditAmount(200L)
    .setOutOfBandAmount(200L)
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
  lines: [
    {
      type: 'invoice_line_item',
      invoice_line_item: '{{INVOICE_LINE_ITEM}}',
      quantity: 2,
    },
  ],
  refund_amount: 100,
  credit_amount: 200,
  out_of_band_amount: 200,
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CreditNoteCreateParams{
  Invoice: stripe.String("{{INVOICE_ID}}"),
  Lines: []*stripe.CreditNoteCreateLineParams{
    &stripe.CreditNoteCreateLineParams{
      Type: stripe.String("invoice_line_item"),
      InvoiceLineItem: stripe.String("{{INVOICE_LINE_ITEM}}"),
      Quantity: stripe.Int64(2),
    },
  },
  RefundAmount: stripe.Int64(100),
  CreditAmount: stripe.Int64(200),
  OutOfBandAmount: stripe.Int64(200),
}
result, err := sc.V1CreditNotes.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CreditNoteCreateOptions
{
    Invoice = "{{INVOICE_ID}}",
    Lines = new List<CreditNoteLineOptions>
    {
        new CreditNoteLineOptions
        {
            Type = "invoice_line_item",
            InvoiceLineItem = "{{INVOICE_LINE_ITEM}}",
            Quantity = 2,
        },
    },
    RefundAmount = 100,
    CreditAmount = 200,
    OutOfBandAmount = 200,
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.CreditNotes;
CreditNote creditNote = service.Create(options);
```

## Voiding credit notes 

You can void a credit note only if it’s on an open invoice. Voiding a credit note reverses its adjustment, increasing the amount due on the invoice by the amount of the credit note. To void a credit note:

```curl
curl -X POST https://api.stripe.com/v1/credit_notes/{{CREDIT_NOTE_ID}}/void \
  -u "<<YOUR_SECRET_KEY>>:"
```

```cli
stripe credit_notes void_credit_note {{CREDIT_NOTE_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

credit_note = client.v1.credit_notes.void_credit_note('{{CREDIT_NOTE_ID}}')
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
credit_note = client.v1.credit_notes.void_credit_note("{{CREDIT_NOTE_ID}}")
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$creditNote = $stripe->creditNotes->voidCreditNote('{{CREDIT_NOTE_ID}}', []);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CreditNoteVoidCreditNoteParams params = CreditNoteVoidCreditNoteParams.builder().build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
CreditNote creditNote =
  client.v1().creditNotes().voidCreditNote("{{CREDIT_NOTE_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const creditNote = await stripe.creditNotes.voidCreditNote('{{CREDIT_NOTE_ID}}');
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CreditNoteVoidCreditNoteParams{}
result, err := sc.V1CreditNotes.VoidCreditNote(
  context.TODO(), "{{CREDIT_NOTE_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.CreditNotes;
CreditNote creditNote = service.VoidCreditNote("{{CREDIT_NOTE_ID}}");
```

## Crediting negative line items 

You can credit a negative amount to a negative `invoice_line_item` using either the `amount` or `quantity` parameters.

For example, if you have an `open` invoice with two line items:

- a positive line item with `quantity=1` and `unit_amount=10000`
- and a negative line item with `quantity=1` and `unit_amount=-5000`.

This example uses the `amount` parameter to credit the full amount of both line items:

```curl
curl https://api.stripe.com/v1/credit_notes \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d invoice={{INVOICE_ID}} \
  -d "lines[0][type]"=invoice_line_item \
  -d "lines[0][invoice_line_item]"={{POSITIVE_INVOICE_LINE_ITEM}} \
  -d "lines[0][amount]"=10000 \
  -d "lines[1][type]"=invoice_line_item \
  -d "lines[1][invoice_line_item]"={{NEGATIVE_INVOICE_LINE_ITEM}} \
  -d "lines[1][amount]"=-5000
```

```cli
stripe credit_notes create  \
  --invoice={{INVOICE_ID}} \
  -d "lines[0][type]"=invoice_line_item \
  -d "lines[0][invoice_line_item]"={{POSITIVE_INVOICE_LINE_ITEM}} \
  -d "lines[0][amount]"=10000 \
  -d "lines[1][type]"=invoice_line_item \
  -d "lines[1][invoice_line_item]"={{NEGATIVE_INVOICE_LINE_ITEM}} \
  -d "lines[1][amount]"=-5000
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

credit_note = client.v1.credit_notes.create({
  invoice: '{{INVOICE_ID}}',
  lines: [
    {
      type: 'invoice_line_item',
      invoice_line_item: '{{POSITIVE_INVOICE_LINE_ITEM}}',
      amount: 10000,
    },
    {
      type: 'invoice_line_item',
      invoice_line_item: '{{NEGATIVE_INVOICE_LINE_ITEM}}',
      amount: -5000,
    },
  ],
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
credit_note = client.v1.credit_notes.create({
  "invoice": "{{INVOICE_ID}}",
  "lines": [
    {
      "type": "invoice_line_item",
      "invoice_line_item": "{{POSITIVE_INVOICE_LINE_ITEM}}",
      "amount": 10000,
    },
    {
      "type": "invoice_line_item",
      "invoice_line_item": "{{NEGATIVE_INVOICE_LINE_ITEM}}",
      "amount": -5000,
    },
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$creditNote = $stripe->creditNotes->create([
  'invoice' => '{{INVOICE_ID}}',
  'lines' => [
    [
      'type' => 'invoice_line_item',
      'invoice_line_item' => '{{POSITIVE_INVOICE_LINE_ITEM}}',
      'amount' => 10000,
    ],
    [
      'type' => 'invoice_line_item',
      'invoice_line_item' => '{{NEGATIVE_INVOICE_LINE_ITEM}}',
      'amount' => -5000,
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CreditNoteCreateParams params =
  CreditNoteCreateParams.builder()
    .setInvoice("{{INVOICE_ID}}")
    .addLine(
      CreditNoteCreateParams.Line.builder()
        .setType(CreditNoteCreateParams.Line.Type.INVOICE_LINE_ITEM)
        .setInvoiceLineItem("{{POSITIVE_INVOICE_LINE_ITEM}}")
        .setAmount(10000L)
        .build()
    )
    .addLine(
      CreditNoteCreateParams.Line.builder()
        .setType(CreditNoteCreateParams.Line.Type.INVOICE_LINE_ITEM)
        .setInvoiceLineItem("{{NEGATIVE_INVOICE_LINE_ITEM}}")
        .setAmount(-5000L)
        .build()
    )
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
  lines: [
    {
      type: 'invoice_line_item',
      invoice_line_item: '{{POSITIVE_INVOICE_LINE_ITEM}}',
      amount: 10000,
    },
    {
      type: 'invoice_line_item',
      invoice_line_item: '{{NEGATIVE_INVOICE_LINE_ITEM}}',
      amount: -5000,
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CreditNoteCreateParams{
  Invoice: stripe.String("{{INVOICE_ID}}"),
  Lines: []*stripe.CreditNoteCreateLineParams{
    &stripe.CreditNoteCreateLineParams{
      Type: stripe.String("invoice_line_item"),
      InvoiceLineItem: stripe.String("{{POSITIVE_INVOICE_LINE_ITEM}}"),
      Amount: stripe.Int64(10000),
    },
    &stripe.CreditNoteCreateLineParams{
      Type: stripe.String("invoice_line_item"),
      InvoiceLineItem: stripe.String("{{NEGATIVE_INVOICE_LINE_ITEM}}"),
      Amount: stripe.Int64(-5000),
    },
  },
}
result, err := sc.V1CreditNotes.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CreditNoteCreateOptions
{
    Invoice = "{{INVOICE_ID}}",
    Lines = new List<CreditNoteLineOptions>
    {
        new CreditNoteLineOptions
        {
            Type = "invoice_line_item",
            InvoiceLineItem = "{{POSITIVE_INVOICE_LINE_ITEM}}",
            Amount = 10000,
        },
        new CreditNoteLineOptions
        {
            Type = "invoice_line_item",
            InvoiceLineItem = "{{NEGATIVE_INVOICE_LINE_ITEM}}",
            Amount = -5000,
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.CreditNotes;
CreditNote creditNote = service.Create(options);
```

This example uses the `quantity` parameter to do the same:

```curl
curl https://api.stripe.com/v1/credit_notes \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d invoice={{INVOICE_ID}} \
  -d "lines[0][type]"=invoice_line_item \
  -d "lines[0][invoice_line_item]"={{POSITIVE_INVOICE_LINE_ITEM}} \
  -d "lines[0][quantity]"=1 \
  -d "lines[1][type]"=invoice_line_item \
  -d "lines[1][invoice_line_item]"={{NEGATIVE_INVOICE_LINE_ITEM}} \
  -d "lines[1][quantity]"=1
```

```cli
stripe credit_notes create  \
  --invoice={{INVOICE_ID}} \
  -d "lines[0][type]"=invoice_line_item \
  -d "lines[0][invoice_line_item]"={{POSITIVE_INVOICE_LINE_ITEM}} \
  -d "lines[0][quantity]"=1 \
  -d "lines[1][type]"=invoice_line_item \
  -d "lines[1][invoice_line_item]"={{NEGATIVE_INVOICE_LINE_ITEM}} \
  -d "lines[1][quantity]"=1
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

credit_note = client.v1.credit_notes.create({
  invoice: '{{INVOICE_ID}}',
  lines: [
    {
      type: 'invoice_line_item',
      invoice_line_item: '{{POSITIVE_INVOICE_LINE_ITEM}}',
      quantity: 1,
    },
    {
      type: 'invoice_line_item',
      invoice_line_item: '{{NEGATIVE_INVOICE_LINE_ITEM}}',
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
credit_note = client.v1.credit_notes.create({
  "invoice": "{{INVOICE_ID}}",
  "lines": [
    {
      "type": "invoice_line_item",
      "invoice_line_item": "{{POSITIVE_INVOICE_LINE_ITEM}}",
      "quantity": 1,
    },
    {
      "type": "invoice_line_item",
      "invoice_line_item": "{{NEGATIVE_INVOICE_LINE_ITEM}}",
      "quantity": 1,
    },
  ],
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$creditNote = $stripe->creditNotes->create([
  'invoice' => '{{INVOICE_ID}}',
  'lines' => [
    [
      'type' => 'invoice_line_item',
      'invoice_line_item' => '{{POSITIVE_INVOICE_LINE_ITEM}}',
      'quantity' => 1,
    ],
    [
      'type' => 'invoice_line_item',
      'invoice_line_item' => '{{NEGATIVE_INVOICE_LINE_ITEM}}',
      'quantity' => 1,
    ],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CreditNoteCreateParams params =
  CreditNoteCreateParams.builder()
    .setInvoice("{{INVOICE_ID}}")
    .addLine(
      CreditNoteCreateParams.Line.builder()
        .setType(CreditNoteCreateParams.Line.Type.INVOICE_LINE_ITEM)
        .setInvoiceLineItem("{{POSITIVE_INVOICE_LINE_ITEM}}")
        .setQuantity(1L)
        .build()
    )
    .addLine(
      CreditNoteCreateParams.Line.builder()
        .setType(CreditNoteCreateParams.Line.Type.INVOICE_LINE_ITEM)
        .setInvoiceLineItem("{{NEGATIVE_INVOICE_LINE_ITEM}}")
        .setQuantity(1L)
        .build()
    )
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
  lines: [
    {
      type: 'invoice_line_item',
      invoice_line_item: '{{POSITIVE_INVOICE_LINE_ITEM}}',
      quantity: 1,
    },
    {
      type: 'invoice_line_item',
      invoice_line_item: '{{NEGATIVE_INVOICE_LINE_ITEM}}',
      quantity: 1,
    },
  ],
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CreditNoteCreateParams{
  Invoice: stripe.String("{{INVOICE_ID}}"),
  Lines: []*stripe.CreditNoteCreateLineParams{
    &stripe.CreditNoteCreateLineParams{
      Type: stripe.String("invoice_line_item"),
      InvoiceLineItem: stripe.String("{{POSITIVE_INVOICE_LINE_ITEM}}"),
      Quantity: stripe.Int64(1),
    },
    &stripe.CreditNoteCreateLineParams{
      Type: stripe.String("invoice_line_item"),
      InvoiceLineItem: stripe.String("{{NEGATIVE_INVOICE_LINE_ITEM}}"),
      Quantity: stripe.Int64(1),
    },
  },
}
result, err := sc.V1CreditNotes.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CreditNoteCreateOptions
{
    Invoice = "{{INVOICE_ID}}",
    Lines = new List<CreditNoteLineOptions>
    {
        new CreditNoteLineOptions
        {
            Type = "invoice_line_item",
            InvoiceLineItem = "{{POSITIVE_INVOICE_LINE_ITEM}}",
            Quantity = 1,
        },
        new CreditNoteLineOptions
        {
            Type = "invoice_line_item",
            InvoiceLineItem = "{{NEGATIVE_INVOICE_LINE_ITEM}}",
            Quantity = 1,
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.CreditNotes;
CreditNote creditNote = service.Create(options);
```

The following restrictions apply:

- The total amount of the credit note must remain positive.
- The total amount credited to a negative line item must be negative.
- The total amount credited to a negative line item can’t be less than the line item amount.

You also can’t credit a negative amount on a `custom_line_item`. We only support negative amounts on `invoice_line_items`.
