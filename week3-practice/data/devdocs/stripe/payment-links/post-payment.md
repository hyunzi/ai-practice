# After a payment link payment

Learn what you can do after receiving a payment link payment.

After you receive a payment through a payment link, you can track payments, manage fulfillment automatically, view payment link metrics, and more.

## Track payments 

After your customer makes a payment using a payment link, you can see it in the payments overview in the [Dashboard](https://dashboard.stripe.com/payments). If you?™re new to Stripe, you receive an email after your first payment. To receive emails for all successful payments, update your notification preferences in your [Personal details](https://dashboard.stripe.com/settings/user) settings.

Stripe creates a new [guest customer](https://docs.stripe.com/payments/checkout/guest-customers.md) for one-time payments and a new [Customer](https://docs.stripe.com/api/customers.md) when selling a subscription or [saving a payment method for future use](https://docs.stripe.com/payment-links/customize.md#save-payment-details-for-future-use).

## Automatically handle fulfillment 

You can automatically handle fulfillment through a Stripe partner or programmatically with the Stripe API:

- **Automation with a Stripe partner:** To automate post-purchase activities like order *fulfillment* (Fulfillment is the process of providing the goods or services purchased by a customer, typically after payment is collected), emailing customers, and recording data to a spreadsheet, you can use a Stripe partner, like [Zapier](https://help.zapier.com/hc/articles/10821467221133), to connect Stripe data to other applications.

- **Handle fulfillment programmatically:** If you?™re interested in handling fulfillment programmatically using the Stripe API and *webhooks* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests), learn how to [fulfill orders after a customer pays](https://docs.stripe.com/checkout/fulfillment.md).

## Change confirmation behavior 

After a successful payment, your customer sees a localized confirmation message thanking them for their purchase. You can customize the confirmation message or redirect to a URL of your choice. To change the confirmation behavior on a payment link, click **After the payment** when [creating](https://dashboard.stripe.com/payment-links/create) or editing a payment link. Under **Confirmation page**, you can choose to replace the default message with a custom one.

You can also choose to redirect your customers to your website instead of providing a confirmation page. If you redirect your customers to your own confirmation page, you can include `{CHECKOUT_SESSION_ID}` in the redirect URL to dynamically pass the customer?™s current Checkout Session ID. This is helpful if you want to tailor the success message on your website based on the information in the Checkout Session. You can also add [UTM codes](https://docs.stripe.com/payment-links/url-parameters.md#track-campaigns-with-utm-codes) as parameters in the query string of the payment link URL. The UTM codes are automatically added to your redirect URL when your customer completes a payment.

## Use payment links with Connect 

With Stripe Connect, you can split a payment with a connected account by taking application fees. Learn how to [use payment links with Connect](https://docs.stripe.com/connect/payment-links.md#collecting-fees).

## Let customers manage their subscriptions 

Create a link that you can send to customers, letting them log in and manage their subscriptions using the *customer portal* (The customer portal is a secure, Stripe-hosted page that lets your customers manage their subscriptions and billing details).

## Send email receipts 

Stripe can automatically send email receipts to your customers after successful payments. You can enable this feature with the [email customers for successful payments](https://dashboard.stripe.com/settings/emails) option in your email receipt settings. To customize your receipt color and logo, go to the [Branding settings](https://dashboard.stripe.com/settings/branding).

To display custom text, use the [description](https://docs.stripe.com/api/payment_intents/create.md#create_payment_intent-description) attribute on the [PaymentIntent](https://docs.stripe.com/api/payment_intents/object.md). Some examples include:

- Description of goods or services provided
- Authorization code
- Subscription information
- Cancellation policies

### Automatically send paid invoices

In addition to ordinary receipts, Payment Links can generate paid invoices as proof of payment. Invoices have more information than receipts. For subscriptions, Stripe generates invoices automatically, but for one-time payments, you need to enable them.

To generate invoices, toggle **Successful payments** on in your [Customer emails settings](https://dashboard.stripe.com/settings/emails). Then, when [creating a Payment Link](https://dashboard.stripe.com/payment-links/create), select **Create an invoice PDF** in the **After payment** tab. You can configure your invoice, including adding a memo, footer, and your tax ID in your [invoice template settings](https://dashboard.stripe.com/settings/billing/invoice).

After the payment completes, Stripe sends an invoice summary with links to download the invoice PDF and invoice receipt to the email address your customer provides during checkout. You can also view the invoice in the [Dashboard](https://dashboard.stripe.com/invoices) or access it programmatically by listening to the [invoice.paid](https://docs.stripe.com/api/events/types.md#event_types-invoice.paid) event through an [event destination](https://docs.stripe.com/event-destinations.md).

> Invoices for delayed notification payment methods such as [Bacs Direct Debit](https://docs.stripe.com/payments/bacs-debit/accept-a-payment.md), [Bank transfers](https://docs.stripe.com/payments/bank-transfers/accept-a-payment.md), [Boleto](https://docs.stripe.com/payments/boleto/accept-a-payment.md), [Canadian pre-authorized debits](https://docs.stripe.com/payments/acss-debit/accept-a-payment.md), [Konbini](https://docs.stripe.com/payments/konbini/accept-a-payment.md), [OXXO](https://docs.stripe.com/payments/oxxo/accept-a-payment.md), [Pay by Bank](https://docs.stripe.com/payments/pay-by-bank/accept-a-payment.md), [SEPA Direct Debit](https://docs.stripe.com/payments/sepa-debit/accept-a-payment.md), or [ACH Direct Debit](https://docs.stripe.com/payments/ach-direct-debit/accept-a-payment.md) might take longer to send because we send the invoice after successful payment, not upon checkout session completion.
![Screenshot of the invoice PDF that customers can download from the invoice summary email](https://b.stripecdn.com/docs-statics-srv/assets/invoice.9e44668032383601eeec362f38293b7a.png)

The downloadable invoice PDF
![Screenshot of the invoice receipt that customers can download from the invoice summary email](https://b.stripecdn.com/docs-statics-srv/assets/invoice_receipt.4f120ee7363f8e7728fa553a8a24aae3.png)

The downloadable invoice receipt
![Screenshot of the invoice summary email Stripe sends](https://b.stripecdn.com/docs-statics-srv/assets/email.560c2666905531b907f7fcd4f1a0a6dd.png)

The customer email with links to the invoice PDF and receipt

### Localization

When using Payment Links, the following factors determine the language of the receipt and invoice:

- If the session creates a [Customer](https://docs.stripe.com/api/customers/object.md) (for example, through the `customer_creation` parameter or `subscription` mode), the browser locale of the user opening the Payment Link URL determines the language of the receipt and invoice.
- If the session doesn?™t create a Customer, the [language setting](https://dashboard.stripe.com/settings/emails) from your Stripe Dashboard is applied instead.

## View payment link metrics 

You can see metrics such as views, sales, and revenue from a given payment link by clicking the **Payments and analytics** tab after selecting a payment link from the [list view](https://dashboard.stripe.com/payment-links). Note that Stripe can delay data for up to 18 hours, and isn?™t available in a sandbox. Analytics aren?™t supported for links that include recurring prices.

You can use this data to better understand how your link is performing and what its conversion rate is:

- **Views**: The number of times your payment link was opened.
- **Sales**: The number of times the payment link was used to complete a purchase.
- **Revenue**: The gross sales volume that the link generated and is always in your default currency regardless of the presentment currency. Stripe converts the amounts using the exchange rate on the day the payment occurs.

## Refund payment links

To refund a payment using the Dashboard:

1. Find the payment you want to refund in the [Payments](https://dashboard.stripe.com/payments) page.
1. Click the overflow menu (?? to the right of the payment, then select **Refund payment**.
1. By default, you issue a full refund. For a partial refund, enter a different refund amount.
1. Select a reason for the refund. If you select **Other**, you must add a note that explains the reason for the refund. Click **Refund**.

Alternatively, you can click  a specific payment and issue a refund from its details page. You can also send [refund receipts](https://docs.stripe.com/receipts.md#refund-receipts) automatically or manually send a receipt for each refund.

### Bulk refunds

The Dashboard supports the bulk refunding of full payments. Select what payments you want to refund by checking the box to the left of each payment?”even over multiple pages of results. Then, click **Refund** and select a reason. You can only issue full refunds in this way; partial refunds must be issued individually.

### Refund timing

It typically takes 5-10 business days for the refund to be processed and reflected on the customer?™s bank statement. If there are any issues with insufficient funds in your Stripe balance, the refund might be set as pending until resolved.
