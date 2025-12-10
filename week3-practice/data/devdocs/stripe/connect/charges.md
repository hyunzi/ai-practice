# Create a charge

Create a charge and split payments between your platform and your sellers or service providers.

To accept a payment from a customer, you must first create a charge. The type of charge you create determines how these funds are split among all parties involved, impacts how the charge appears on the customer’s bank or billing statement (with your platform’s information or your user’s), and determines which account Stripe debits for refunds and chargebacks.

## Charge types 

Charges can be [direct charges](https://docs.stripe.com/connect/charges.md#direct), which are payments made directly to a connected account, or indirect charges, which are payments made to your platform. Indirect charges include [destination charges](https://docs.stripe.com/connect/charges.md#destination) and [separate charges and transfers](https://docs.stripe.com/connect/charges.md#separate-charges-transfers), which differ in the way they transfer funds from your platform to connected accounts.

There are many factors to consider when choosing a charge type, as listed in the table below. Your platform’s business model is particularly important because it can affect how funds flow through Stripe. To review charge type recommendations for your business, refer to your [platform profile](https://dashboard.stripe.com/connect/settings/profile).

| Charge type                                                                                         | Use when                                                                                                                                                                                                                                                                                                    | Examples                                                                                                                                                                |
| --------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| [Direct charges](https://docs.stripe.com/connect/direct-charges.md)                                 | - Your connected accounts transact directly with their customers, who are often unaware of your platform’s existence.
  - Each transaction involves a single connected account and a single customer.
  - You want to choose whether Stripe debits fees from your connected accounts or from your platform. | - An e-commerce platform for independent sellers.
  - A SaaS platform, such as an accounting platform that enables invoice payments.                                    |
| [Destination charges](https://docs.stripe.com/connect/destination-charges.md)                       | - Customers transact with your platform for products or services provided by your connected accounts.
  - Each transaction involves a single connected account and a single customer.
  - Stripe debits fees from your platform account.                                                                    | - A branded service that uses independent contractors, such as a rideshare app.
  - A services marketplace, such as a website that matches contractors with homeowners. |
| [Separate charges and transfers](https://docs.stripe.com/connect/separate-charges-and-transfers.md) | - Individual transactions can involve multiple connected accounts.
  - You can create a charge before knowing the connected account.
  - You can create a charge before being able to transfer the funds.
  - Stripe debits fees&nbsp;from your platform account.                                           | - An e-commerce marketplace that allows a single shopping cart for goods sold by multiple businesses                                                                    |

You can use a single approach, more than one approach, or switch approaches as appropriate for your organization.

### Direct charges 

Create a charge directly on a connected account. The account’s customers are often unaware of your platform’s existence. You can add an application fee, which transfers to your platform’s account balance when the connected account collects the payment.

> Your connected accounts must have the [card_payments capability](https://docs.stripe.com/connect/account-capabilities.md#card-payments) active in order to use direct charges.

This charge type is best suited for platforms providing software as a service. For example, Shopify provides tools for building online storefronts, and Thinkific enables educators to sell online courses.

With this charge type:

- You create a charge on your connected account, so the payment appears in the connected account’s balance, not in your platform’s balance.
- The connected account’s balance increases with every charge.
- Funds always settle in the country of the connected account.
- Your platform’s balance increases with any application fees you collect for a charge.
- Refunds and chargebacks reduce the connected account’s balance.
- You can choose whether to have Stripe debit fees directly from connected accounts or from your platform account.
- You can use Stripe Managed Risk if you meet these [additional requirements](https://docs.stripe.com/connect/risk-management/managed-risk.md#requirements-to-use-stripe-managed-risk).
![Direct charges funds flow diagram](https://b.stripecdn.com/docs-statics-srv/assets/direct_charges.a2a8b68037ac95fe22140d6dde9740d3.svg)

How are funds routed with direct charges?

### Destination charges 

Create a charge on the platform and immediately transfer funds to the connected account. You decide whether some or all of those funds are transferred, and whether to deduct an application fee.

This charge type is best suited for marketplaces, such as a home rental marketplace or a ridesharing app.

With this charge type:

- You create a charge on your platform, so the payment appears in your platform’s balance. A portion of the funds immediately transfers to the connected account’s balance (see the funds flow diagrams below).
- The portion of the funds that remains in your platform balance represents the fee you charge your connected account.
- Refunds and chargebacks reduce your platform’s balance.
- Stripe debits fees from your platform’s balance.
![Destination charges balance funds flow diagram](https://b.stripecdn.com/docs-statics-srv/assets/platform_charges.6a14fd660d7433ba617e816ff09f10b5.svg)

Send the balance after platform fee to your connected account.
![Destination charges funds flow diagram](https://b.stripecdn.com/docs-statics-srv/assets/application_fee_amount.837aa2339469b3c1a4319672971c1367.svg)

Send the full payment amount to your connected account, then charge your platform fee.

> In most scenarios, destination charges are only supported if both your platform and the connected account are in the same region (for example, both in the US). For cross-region support, you can specify the [settlement merchant](https://docs.stripe.com/connect/destination-charges.md#settlement-merchant) as the connected account using the [on_behalf_of](https://docs.stripe.com/api/payment_intents/object.md#payment_intent_object-on_behalf_of) attribute on the charge. For more information about cross-region support, see [Cross-border transfers](https://docs.stripe.com/connect/account-capabilities.md#transfers-cross-border).

### Separate charges and transfers 

Create charges on your platform and split funds between multiple connected accounts, or hold them when you don’t know the specific user at the time of the charge. The charge on your platform account is decoupled from the transfers to your connected accounts.

This charge type is best suited for marketplaces that need to split payments between multiple parties, such as DoorDash, a restaurant delivery platform.

For [Express](https://docs.stripe.com/connect/express-accounts.md) and [Custom](https://docs.stripe.com/connect/custom-accounts.md) accounts, Stripe recommends that you create [separate charges and transfers](https://docs.stripe.com/connect/separate-charges-and-transfers.md) if destination charges don’t meet your business needs.

With this charge type:

- You create a charge on your platform’s account first. Create a separate transfer to move funds to your connected account. The payment appears as a charge on your account and there’s also a transfer to a connected account (amount determined by you), which is withdrawn from your [account balance](https://docs.stripe.com/connect/account-balances.md).
- You can transfer funds to multiple connected accounts.
- Your account balance is debited for the cost of the Stripe fees, refunds, and chargebacks.
![Transfer funds flow diagram](https://b.stripecdn.com/docs-statics-srv/assets/charges_transfers.a95f5bf398651fba0fb303e32a742546.svg)

Transfer funds to multiple connected accounts.

> In most scenarios, your platform and any connected account must be in the same region. Attempting to transfer funds across a disallowed border returns an error. For information about cross-region support, see [Cross-border transfers](https://docs.stripe.com/connect/account-capabilities.md#transfers-cross-border).

Using separate charges and transfers requires a more complex *Connect* (Connect is Stripe's solution for multi-party businesses, such as marketplace or software platforms, to route payments between sellers, customers, and other recipients) integration.

Use this charge type if your business has specific use cases:

- A one-to-many relationship. For example, a payment made to a delivery service needs to be split between the store (the source of the items being delivered) and the delivery person.
- A many-to-one relationship. For example, a carpool trip with a ride-hailing service.
- Charges created before the destination account is known. For example, a janitorial service could process a payment before deciding which janitor to assign to the job.
- Need to transfer funds before receiving a payment, or while the charge is pending. For example, an ad network needs to purchase ad space before they can sell ad time or before receiving any payment from customers.
- Transfer amounts greater than the associated payments. For example, a platform provides a discount to its customer but pays its user the full amount.

In some cases, the transfer amount can be greater than the charge amount, or the transfer is made before the payment is processed. You must monitor your account balance carefully to make sure it has enough available funds to cover the transfer amount. You can also associate a transfer with a charge so the transfer doesn’t occur until the funds from that charge are available.

#### on_behalf_of parameter 

To make the connected account the business of record for the payment, use the `on_behalf_of` parameter. When `on_behalf_of` is set to the ID of the connected account, Stripe automatically:

- Settles charges in the country of the specified account to minimize declines and avoid [currency conversions](https://docs.stripe.com/connect/currencies/fx-quotes-api.md#currency-conversions).
- Uses the fee structure for the connected account’s country.
- Uses the [connected account’s statement descriptor](https://docs.stripe.com/connect/statement-descriptors.md).
- Uses the connected account’s address and phone number (rather than the platform’s) on the customer’s statement if the connected account is in a different country than the platform.
- Pays out a connected account, depending on the days specified in its `delays_days` setting.

## Stripe fees

There are two components to Stripe fees with Connect: which pricing plan applies to the payment and which account pays Stripe fees.

When using Direct charges, you can choose how Stripe fees are billed to your connected accounts.

[Read more about fee billing behaviors with Direct charges.](https://docs.stripe.com/connect/direct-charges-fee-payer-behavior.md)

Destination charges and separate charges and transfers typically use the platform’s pricing plan and are assessed on the platform. When the `on_behalf_of` field is set, the country of the connected account is used to determine the country specific fees charged to your platform account.

For more information on Connect fees and how to request custom pricing, please see [Connect pricing](https://stripe.com/connect/pricing).

## Refunds 

You can issue a [refund](https://docs.stripe.com/api/refunds.md) to pay a customer back for money spent on a returned good or to compensate for unsatisfactory service. Stripe handles refunds for each of the following charge types:

| Charge Types                       | Pending Refunds                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
| ---------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Direct charges**                 | Stripe debits the refund amount from the connected account’s balance directly when you create a refund.

  If the connected account’s balance is insufficient, we set the `Refund` status to `pending`. When the connected account’s balance has enough funds, Stripe automatically processes pending refunds in the order they were created and updates their status to `successful`.                                                                                                                            |
| **Separate charges and transfers** | Stripe debits your platform balance for the refund amount. You can reverse the transfers made to your connected accounts to recover your refund cost.

  If your platform’s account balance doesn’t have the funds when you [issue the Refund](https://docs.stripe.com/connect/separate-charges-and-transfers.md#issue-refunds), we set the `refund` status to `pending`. When you platform balance has enough funds, Stripe automatically processes pending refunds and updates their status to `successful`.    |
| **Destination charges**            | Stripe debits your platform balance for the refund amount. You can reverse the transfers made to your connected accounts to recover your refund cost.

  If your platform’s account balance doesn’t have the funds when you [issue the Refund](https://docs.stripe.com/connect/separate-charges-and-transfers.md#issue-refunds), we set the `refund` status to `pending`. When your platform’s balance has enough funds, Stripe automatically processes pending refunds and updates their status to `successful`. |

## Disputes and chargebacks

For disputes on payments created using [direct charges](https://docs.stripe.com/connect/direct-charges.md), Stripe debits the disputed amount from the connected account’s balance, not your platform’s balance. Stripe can bill the dispute fee to either the platform or the connected account, depending on the connected account’s configuration. For more detail about how we bill fees for disputes on direct charges, see [Fee behavior on connected accounts](https://docs.stripe.com/connect/direct-charges-fee-payer-behavior.md).

For disputes where payments were created on your platform using [destination charges](https://docs.stripe.com/connect/destination-charges.md) or [separate charges and transfers](https://docs.stripe.com/connect/separate-charges-and-transfers.md), with or without `on_behalf_of`, your platform balance is automatically debited for the disputed amount and fee. When this happens, your platform can attempt to recover funds from the connected account by reversing the transfer either through the [Dashboard](https://dashboard.stripe.com/test/transfers) or by [creating a transfer reversal](https://docs.stripe.com/api.md#create_transfer_reversal).

> Creating payments using destination charges or separate charges or transfers, with or without `on_behalf_of`, always debits refund and disputed amounts from your platform balance, even when *Stripe is liable for negative balances* (The responsibility for managing risk and recovering negative balances on connected accounts. Stripe or the Connect platform can be liable for negative balances on connected accounts) on your connected accounts.

If there’s a negative balance on the connected account, Stripe attempts to debit the external account on file for the connected account only if `debit_negative_balances` is set to `true`.

For more details, see [Disputes and fraud](https://docs.stripe.com/disputes.md) and [Dispute categories](https://docs.stripe.com/disputes/categories.md). You can also use [Fraud Stripe Apps](https://marketplace.stripe.com/categories/fraud) to automate dispute management and handle chargebacks.

## See also

- [Create direct charges](https://docs.stripe.com/connect/direct-charges.md)
- [Create destination charges](https://docs.stripe.com/connect/destination-charges.md)
- [Create separate charges and transfers](https://docs.stripe.com/connect/separate-charges-and-transfers.md)
- [Set statement descriptors](https://docs.stripe.com/connect/statement-descriptors.md)
- [Integrate tax calculation and collection](https://docs.stripe.com/tax/connect.md)
