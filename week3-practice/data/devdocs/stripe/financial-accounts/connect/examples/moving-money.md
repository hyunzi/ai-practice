# Using Financial Accounts for platforms to move money

Learn how to use SetupIntents and PaymentMethods, and how to verify bank accounts with Financial Accounts for platforms.

Homebox is a fictitious vertical SaaS that builds software for home services companies such as HVAC, cleaners, and plumbers. This example integration describes some basic money movement using the treasury endpoints of the [Stripe API](https://docs.stripe.com/api.md). To see how the company would set up a financial account and issue payment cards, see [Using Financial Accounts for platforms to set up financial accounts and cards](https://docs.stripe.com/financial-accounts/connect/examples/financial-accounts.md).

## Using external bank accounts

Financial Accounts for platforms provides platforms with a suite of account creation and money movement API endpoints to help store, manage, and move connected account funds. Platforms can fund a financial account, and move money between financial accounts and external bank accounts.

The Stripe API provides `InboundTransfer` and `OutboundTransfer` to facilitate moving money between connected accounts’ external bank accounts and their financial accounts. The Stripe API also provides `OutboundPayment` to facilitate moving money from connected accounts’ financial accounts to third-party external bank accounts. All of these objects can use `PaymentMethods` to store external bank account details such as routing numbers and account numbers.

## PaymentMethods explained

Within the Stripe API, you can save payment method information using a `PaymentMethod` object. For example, Homebox might save their vendors’ accounts as `PaymentMethods` to send money without having to re-enter and collect their information.

You can attach `PaymentMethods` that contain external bank account information to a customer (for sending money to a third party) or to a Stripe account (for pulling money from or sending money to an external bank account belonging to the Stripe accountholder). The “customer” refers to the `Customer` object in the Stripe API, which defines any third party. In Financial Accounts for platforms usage, the customer is generally a vendor receiving payments from the Stripe accountholder rather than making payments to the Stripe accountholder. Use the `SetupIntent` object to set up both customer-attached and account-attached `PaymentMethod` objects.

The relevant API objects that you can use with a `PaymentMethod` depend on how they’re attached:

- Customer-attached: Use `OutboundPayments`.
- Account-attached: Use `InboundTransfers` and `OutboundTransfers`.
![Flowchart with external bank account on left and flow moving to SetupIntent then PaymentMethod and attaching to customer or account.](https://b.stripecdn.com/docs-statics-srv/assets/paymentmethods.f429ec4e87b87636c0e2b34c26a69c83.png)

PaymentMethod flow

## InboundTransfers and OutboundTransfers overview

You can pull funds from a Stripe account’s external bank account using an `InboundTransfer` and push funds to their external bank account using an `OutboundTransfer`.

To successfully pull funds into a financial account with an `InboundTransfer`, external bank accounts must be verified by the Stripe accountholder. Bank accounts that are used to send funds out of a financial account with an `OutboundTransfer` don’t need to be verified.

When an external bank account is used for either `InboundTransfers` or `OutboundTransfers`, you need to attach the corresponding `PaymentMethod` to the Stripe account rather than to a customer. You do this by using the `attach_to_self=true` parameter rather than the `customer` parameter when creating the `PaymentMethod` using a `SetupIntent`.
![Flow chart with external bank on the left and flow pointing to SetupIntent to PaymentMethod and attaching to Stripe account.](https://b.stripecdn.com/docs-statics-srv/assets/iot.38c049d39deca401a3341b34d72189fe.png)

InboundTransfers and OutboundTransfers flow.

## OutboundPayments

Platforms use the `OutboundPayment` API to send funds from a financial account to an external bank account owned by a third party.

You must attach `PaymentMethods` to a customer to use them with `OutboundPayments`. You don’t need to verify bank accounts used for `OutboundPayments`.
![](https://b.stripecdn.com/docs-statics-srv/assets/iot-customer.5476c499693c606ead280fbae61ebe4a.png)

`OutboundPayments` flow

## Adding an external bank account

Homebox wants to link their customers’ own external bank accounts to their financial accounts. The Homebox connected accounts want to keep all of their business capital in their financial accounts and use inbound transfers to pull money from their external account into their financial account. To enable their connected accounts to transfer money to and from their external accounts, Homebox creates a `SetupIntent` with the required parameters and attaches it to an account-attached `PaymentMethod`:

```curl
curl https://api.stripe.com/v1/setup_intents \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d attach_to_self=true \
  -d "flow_directions[]"=inbound \
  -d "flow_directions[]"=outbound \
  -d "payment_method_types[]"=us_bank_account \
  -d "payment_method_data[type]"=us_bank_account \
  -d "payment_method_data[us_bank_account][routing_number]"=110000000 \
  -d "payment_method_data[us_bank_account][account_number]"=000123456789 \
  -d "payment_method_data[us_bank_account][account_holder_type]"=company \
  -d "payment_method_data[billing_details][name]"="Company Corp" \
  -d confirm=true \
  -d "mandate_data[customer_acceptance][type]"=online \
  -d "mandate_data[customer_acceptance][online][ip_address]"="123.123.123.123" \
  --data-urlencode "mandate_data[customer_acceptance][online][user_agent]"="curl/1.2.3"
```

```cli
stripe setup_intents create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --attach-to-self=true \
  -d "flow_directions[0]"=inbound \
  -d "flow_directions[1]"=outbound \
  -d "payment_method_types[0]"=us_bank_account \
  -d "payment_method_data[type]"=us_bank_account \
  -d "payment_method_data[us_bank_account][routing_number]"=110000000 \
  -d "payment_method_data[us_bank_account][account_number]"=000123456789 \
  -d "payment_method_data[us_bank_account][account_holder_type]"=company \
  -d "payment_method_data[billing_details][name]"="Company Corp" \
  --confirm=true \
  -d "mandate_data[customer_acceptance][type]"=online \
  -d "mandate_data[customer_acceptance][online][ip_address]"="123.123.123.123" \
  -d "mandate_data[customer_acceptance][online][user_agent]"="curl/1.2.3"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

setup_intent = client.v1.setup_intents.create(
  {
    attach_to_self: true,
    flow_directions: ['inbound', 'outbound'],
    payment_method_types: ['us_bank_account'],
    payment_method_data: {
      type: 'us_bank_account',
      us_bank_account: {
        routing_number: '110000000',
        account_number: '000123456789',
        account_holder_type: 'company',
      },
      billing_details: {name: 'Company Corp'},
    },
    confirm: true,
    mandate_data: {
      customer_acceptance: {
        type: 'online',
        online: {
          ip_address: '123.123.123.123',
          user_agent: 'curl/1.2.3',
        },
      },
    },
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
setup_intent = client.v1.setup_intents.create(
  {
    "attach_to_self": True,
    "flow_directions": ["inbound", "outbound"],
    "payment_method_types": ["us_bank_account"],
    "payment_method_data": {
      "type": "us_bank_account",
      "us_bank_account": {
        "routing_number": "110000000",
        "account_number": "000123456789",
        "account_holder_type": "company",
      },
      "billing_details": {"name": "Company Corp"},
    },
    "confirm": True,
    "mandate_data": {
      "customer_acceptance": {
        "type": "online",
        "online": {"ip_address": "123.123.123.123", "user_agent": "curl/1.2.3"},
      },
    },
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$setupIntent = $stripe->setupIntents->create(
  [
    'attach_to_self' => true,
    'flow_directions' => ['inbound', 'outbound'],
    'payment_method_types' => ['us_bank_account'],
    'payment_method_data' => [
      'type' => 'us_bank_account',
      'us_bank_account' => [
        'routing_number' => '110000000',
        'account_number' => '000123456789',
        'account_holder_type' => 'company',
      ],
      'billing_details' => ['name' => 'Company Corp'],
    ],
    'confirm' => true,
    'mandate_data' => [
      'customer_acceptance' => [
        'type' => 'online',
        'online' => [
          'ip_address' => '123.123.123.123',
          'user_agent' => 'curl/1.2.3',
        ],
      ],
    ],
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SetupIntentCreateParams params =
  SetupIntentCreateParams.builder()
    .setAttachToSelf(true)
    .addFlowDirection(SetupIntentCreateParams.FlowDirection.INBOUND)
    .addFlowDirection(SetupIntentCreateParams.FlowDirection.OUTBOUND)
    .addPaymentMethodType("us_bank_account")
    .setPaymentMethodData(
      SetupIntentCreateParams.PaymentMethodData.builder()
        .setType(SetupIntentCreateParams.PaymentMethodData.Type.US_BANK_ACCOUNT)
        .setUsBankAccount(
          SetupIntentCreateParams.PaymentMethodData.UsBankAccount.builder()
            .setRoutingNumber("110000000")
            .setAccountNumber("000123456789")
            .setAccountHolderType(
              SetupIntentCreateParams.PaymentMethodData.UsBankAccount.AccountHolderType.COMPANY
            )
            .build()
        )
        .setBillingDetails(
          SetupIntentCreateParams.PaymentMethodData.BillingDetails.builder()
            .setName("Company Corp")
            .build()
        )
        .build()
    )
    .setConfirm(true)
    .setMandateData(
      SetupIntentCreateParams.MandateData.builder()
        .setCustomerAcceptance(
          SetupIntentCreateParams.MandateData.CustomerAcceptance.builder()
            .setType(SetupIntentCreateParams.MandateData.CustomerAcceptance.Type.ONLINE)
            .setOnline(
              SetupIntentCreateParams.MandateData.CustomerAcceptance.Online.builder()
                .setIpAddress("123.123.123.123")
                .setUserAgent("curl/1.2.3")
                .build()
            )
            .build()
        )
        .build()
    )
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

SetupIntent setupIntent = client.v1().setupIntents().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const setupIntent = await stripe.setupIntents.create(
  {
    attach_to_self: true,
    flow_directions: ['inbound', 'outbound'],
    payment_method_types: ['us_bank_account'],
    payment_method_data: {
      type: 'us_bank_account',
      us_bank_account: {
        routing_number: '110000000',
        account_number: '000123456789',
        account_holder_type: 'company',
      },
      billing_details: {
        name: 'Company Corp',
      },
    },
    confirm: true,
    mandate_data: {
      customer_acceptance: {
        type: 'online',
        online: {
          ip_address: '123.123.123.123',
          user_agent: 'curl/1.2.3',
        },
      },
    },
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SetupIntentCreateParams{
  AttachToSelf: stripe.Bool(true),
  FlowDirections: []*string{
    stripe.String(stripe.SetupIntentFlowDirectionInbound),
    stripe.String(stripe.SetupIntentFlowDirectionOutbound),
  },
  PaymentMethodTypes: []*string{stripe.String("us_bank_account")},
  PaymentMethodData: &stripe.SetupIntentCreatePaymentMethodDataParams{
    Type: stripe.String("us_bank_account"),
    USBankAccount: &stripe.SetupIntentCreatePaymentMethodDataUSBankAccountParams{
      RoutingNumber: stripe.String("110000000"),
      AccountNumber: stripe.String("000123456789"),
      AccountHolderType: stripe.String("company"),
    },
    BillingDetails: &stripe.SetupIntentCreatePaymentMethodDataBillingDetailsParams{
      Name: stripe.String("Company Corp"),
    },
  },
  Confirm: stripe.Bool(true),
  MandateData: &stripe.SetupIntentCreateMandateDataParams{
    CustomerAcceptance: &stripe.SetupIntentCreateMandateDataCustomerAcceptanceParams{
      Type: stripe.String("online"),
      Online: &stripe.SetupIntentCreateMandateDataCustomerAcceptanceOnlineParams{
        IPAddress: stripe.String("123.123.123.123"),
        UserAgent: stripe.String("curl/1.2.3"),
      },
    },
  },
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1SetupIntents.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new SetupIntentCreateOptions
{
    AttachToSelf = true,
    FlowDirections = new List<string> { "inbound", "outbound" },
    PaymentMethodTypes = new List<string> { "us_bank_account" },
    PaymentMethodData = new SetupIntentPaymentMethodDataOptions
    {
        Type = "us_bank_account",
        UsBankAccount = new SetupIntentPaymentMethodDataUsBankAccountOptions
        {
            RoutingNumber = "110000000",
            AccountNumber = "000123456789",
            AccountHolderType = "company",
        },
        BillingDetails = new SetupIntentPaymentMethodDataBillingDetailsOptions
        {
            Name = "Company Corp",
        },
    },
    Confirm = true,
    MandateData = new SetupIntentMandateDataOptions
    {
        CustomerAcceptance = new SetupIntentMandateDataCustomerAcceptanceOptions
        {
            Type = "online",
            Online = new SetupIntentMandateDataCustomerAcceptanceOnlineOptions
            {
                IpAddress = "123.123.123.123",
                UserAgent = "curl/1.2.3",
            },
        },
    },
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.SetupIntents;
SetupIntent setupIntent = service.Create(options, requestOptions);
```

To send and receive money through `OutboundTransfers` or `InboundTransfers`, Homebox must specify an external bank account with the `payment_method_data[us_bank_account]` parameter. Before creating the live external bank account link, Homebox tests the flow using the [test account numbers](https://docs.stripe.com/payments/ach-direct-debit/set-up-payment.md?platform=web&payment-ui=stripe-hosted#test-account-numbers) Stripe provides.

As shown in the preceding example, Homebox has defined the external bank account (`payment_method_data[us_bank_account]`) and set `attach_to_self` as `true`, which enables the bank account information to be associated with the Stripe account holder (rather than a third party). When the platform sends the request, the API responds with a `SetupIntent`:

```json
{
  "id": "{{SETUP_INTENT_ID}}",
  "object": "setup_intent",
  "application": "{{APPLICATION_ID}}",
  "attach_to_self": true,
  "cancellation_reason": null,
  "client_secret": "{{SETUP_INTENT_SECRET}}",
  "created": 1642520861,
  "customer": null,
  "description": null,
  "flow_directions": [
    "inbound",
    "outbound"
  ],
  "last_setup_error": null,
  "latest_attempt": "{{ATTEMPT_ID}}",
  "livemode": false,
  "mandate": "{{MANDATE_ID}}",
  "metadata": {
  },
  "next_action": {
    "type": "verify_with_microdeposits",
    "verify_with_microdeposits": {
      "arrival_date": 1642579200,"hosted_verification_url": "https://payments.stripe.com/microdeposit/sacs_test_xxx",
      "microdeposit_type": "amounts"
    }
  },
  "on_behalf_of": null,"payment_method": "{{PAYMENT_METHOD_ID}}",
  "payment_method_options": {
    "us_bank_account": {
      "verification_method": "automatic"
    }
  },
  "payment_method_types": [
    "us_bank_account"
  ],
  "single_use_mandate": null,
  "status": "requires_confirmation",
  "usage": "off_session"
}

```

## Verifying an external bank account

Homebox linked an external bank account in the previous section. The bank account must be verified before Homebox can use it for `InboundTransfers`. Bank account verification enables the account owner to confirm ownership of the external bank account. To verify, Homebox can use [Stripe Financial Connections](https://docs.stripe.com/financial-accounts/connect/examples/moving-money.md#connections) for instant verification, or [microdeposits](https://docs.stripe.com/financial-accounts/connect/examples/moving-money.md#microdeposits) (which take more time).

### Stripe Financial Connections 

You can use [Financial Connections](https://docs.stripe.com/financial-connections.md) to instantly verify an owned bank account. Financial Connections is a Stripe hosted flow integrated on the application’s client- and server-side.

Three steps necessary for Homebox to collect and verify bank account information with Financial Connections:

1. Create a `SetupIntent` with the property `attach_to_self` (replacing the `customer_id` property), and the value `instant` for the `verification_method` property.

1. Provide the `client_secret` to the front-end application to use `stripe.collectBankAccountForSetup` to collect bank account details, create a `PaymentMethod`, and attach that `PaymentMethod` to the `SetupIntent`.

   > The account holder’s name in the `billing_details` parameter is required to create a US bank account `PaymentMethod`.

1. Display the mandate terms to collect the authorization for the `PaymentMethod` usage.

For step 1, Homebox develops the following code to create a `SetupIntent` server-side and pass the information to a handlebars.js template. This code assumes that an application has a user logged in to Stripe and that it passes the Stripe Account ID as part of the [`Session` object](https://docs.stripe.com/api/financial_connections/sessions/object.md) (`req.session.accountId`).

```javascript
const stripe = require("stripe")(process.env.STRIPE_SECRET_KEY);
router.get('/add_bank_account', async (req, res) => {
  const account = await stripe.accounts.retrieve(req.session.accountId);
  const setupIntent = await stripe.setupIntents.create({
    attach_to_self: true,
    flow_directions: ['inbound', 'outbound'],
    payment_method_types: ['us_bank_account'],
    payment_method_options: {us_bank_account: {verification_method: "automatic"}}
  },{
    stripeAccount: account.id,
  });
  let client_secret= setupIntent.client_secret;
  res.render('add_bank_account', { account: account, client_secret: client_secret, stripe_pk: process.env.STRIPE_PUBLISHABLE_KEY });
});
```

Step 2 begins when Homebox passes the following data to the render function:

- The Stripe account ID.
- The client secret obtained from the `SetupIntent` that’s used as the identifier.
- The platform’s publishable API key.

In this example, the client-side page renders with a button for the user to validate their bank account through Financial Connections.
![Screenshot of a modal titled Add a bank account with a button labeled Save Payment Details](https://b.stripecdn.com/docs-statics-srv/assets/add-bank-account-modal.0b727f65d3c6691f3bef211c1a45cb83.png)

Modal to add a bank account

Homebox creates the following code to drive the logic behind the preceding button.

```javascript
const getBankAccount = async e => {
  paymentMethodButton.setAttribute("disabled", "disabled");
  // Calling this method triggers the Financial Connections modal to display.
  const response = await stripe.collectBankAccountForSetup("{{ client_secret }}", {
    billing_details: {
      name: "John Doe",
      email: "test@test.com",
    },
  });
  if (response.error)
  {
    console.error(response.error);
  }
  if (response.status == "requires_confirmation")
  {
    //Show confirmation modal
    AcceptBankAccountModal.classList.remove("hidden");
  }
};
```

In this code example, the script calls the `collectUsBankAccountForSetup` method and passes the `client_secret` from the `SetupIntent`. An application dialog leads the user through linking their bank account.
![Screenshot of a modal going through the workflow of adding a bank account. First state of modal shows the authorize step with an Agree button and the next state shows the highlighted account with a Link Account button.](https://b.stripecdn.com/docs-statics-srv/assets/add-account-flow.cfd2209695ca186cd617f682319e564c.png)

Workflow for adding a bank account

Step 3 begins with the successful completion of the preceding workflow, as the `SetupIntent` status changes to `requires_confirmation` and a modal displays an [authorization message](https://docs.stripe.com/payments/ach-direct-debit/set-up-payment.md?platform=web#web-collect-mandate-and-submit) for the user to confirm.
![Screenshot of a modal displaying the authorization confirmation language with an Accept button at the bottom and an x in the top left to cancel.](https://b.stripecdn.com/docs-statics-srv/assets/accept-modal.6db14c686b9ec87ff471a18d2f9d2abf.png)

Authorization message

After the user clicks **Accept**, the code calls the `confirmUsBankAccountSetup` method and the bank account is stored as verified. The bank account can now be used for `InboundTransfers`.

```javascript
const acceptBankAccount = async e => {
  acceptButton.setAttribute("disabled", "disabled");
  const response = await stripe.confirmUsBankAccountSetup("{{ client_secret }}");
  if (response.setupIntent.status == "requires_payment_method") {
    // Confirmation failed. Attempt again with a different payment method.
    console.log("Requires payment method")
  } else if (response.setupIntent.status == "succeeded") {
    // Confirmation succeeded! The account is now saved.
    // Display a message to customer.
    AcceptBankAccountModal.classList.add("hidden");
    console.log("Account added");
  } else if (response.setupIntent.next_action?.type == "verify_with_microdeposits") {
    // The account needs to be verified through microdeposits.
    // Display a message to consumer with next steps (consumer waits for
    // microdeposits, then enters an amount on a page sent to them through email).
    console.log("The account needs to be verified with microdeposits")
  }
};
```

### Microdeposits

Microdeposits are small sums of money, typically a fraction of a US dollar, that Stripe deposits into an external bank account. Owners of the account can verify ownership of the account by confirming the exact amount of those deposits.

The `SetupIntent` object that Homebox created in the previous section includes a `PaymentMethod` ID.

```json
{
  "id": "{{SETUP_INTENT_ID}}",
  …
  "on_behalf_of": null,"payment_method": "{{PAYMENT_METHOD_ID}}",
  "payment_method_options": {
…
 }
}

```

The `SetupIntent` also includes a `next_action` object with a URL defined in the `hosted_verification_url` value.

```json
{
  "id": "{{SETUP_INTENT_ID}}",
…
   "next_action": {
    "type": "verify_with_microdeposits",
    "verify_with_microdeposits": {
      "arrival_date": 1642579200,"hosted_verification_url": "https://payments.stripe.com/microdeposit/sacs_test_xxx",
      "microdeposit_type": "amounts"
    }
…
 }
```

Homebox provides the URL to the connected account owner to complete verification. The owner must follow the URL to verify receipt by entering the associated descriptor code of the microdeposit (in testing environments, use the value `SM11AA`).
![Microdeposit verification dialog showing one cent deposited into an account with a prompt to enter descriptor code and a verify button.](https://b.stripecdn.com/docs-statics-srv/assets/microdeposits.0b67c00bb4028265c9ed9dbe300f5b81.png)

Microdeposit verification

## Using PaymentMethods with InboundTransfers

Homebox can begin creating `InboundTransfers` using a bank account verified with one of the previously described methods.

The following request transfers 200 USD using an account-attached payment method into the financial account with the provided ID. The `Stripe-Account` header value identifies the Stripe account that owns both the financial account and the payment method.

```curl
curl https://api.stripe.com/v1/treasury/inbound_transfers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d origin_payment_method="{{PAYMENTMETHOD_ID}}" \
  -d financial_account="{{TREASURYFINANCIALACCOUNT_ID}}" \
  -d amount=20000 \
  -d currency=usd \
  -d description="Funds for repair" \
  -d statement_descriptor="Invoice 12"
```

```cli
stripe treasury inbound_transfers create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --origin-payment-method="{{PAYMENTMETHOD_ID}}" \
  --financial-account="{{TREASURYFINANCIALACCOUNT_ID}}" \
  --amount=20000 \
  --currency=usd \
  --description="Funds for repair" \
  --statement-descriptor="Invoice 12"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

inbound_transfer = client.v1.treasury.inbound_transfers.create(
  {
    origin_payment_method: '{{PAYMENTMETHOD_ID}}',
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
    amount: 20000,
    currency: 'usd',
    description: 'Funds for repair',
    statement_descriptor: 'Invoice 12',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
inbound_transfer = client.v1.treasury.inbound_transfers.create(
  {
    "origin_payment_method": "{{PAYMENTMETHOD_ID}}",
    "financial_account": "{{TREASURYFINANCIALACCOUNT_ID}}",
    "amount": 20000,
    "currency": "usd",
    "description": "Funds for repair",
    "statement_descriptor": "Invoice 12",
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$inboundTransfer = $stripe->treasury->inboundTransfers->create(
  [
    'origin_payment_method' => '{{PAYMENTMETHOD_ID}}',
    'financial_account' => '{{TREASURYFINANCIALACCOUNT_ID}}',
    'amount' => 20000,
    'currency' => 'usd',
    'description' => 'Funds for repair',
    'statement_descriptor' => 'Invoice 12',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

InboundTransferCreateParams params =
  InboundTransferCreateParams.builder()
    .setOriginPaymentMethod("{{PAYMENTMETHOD_ID}}")
    .setFinancialAccount("{{TREASURYFINANCIALACCOUNT_ID}}")
    .setAmount(20000L)
    .setCurrency("usd")
    .setDescription("Funds for repair")
    .setStatementDescriptor("Invoice 12")
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

InboundTransfer inboundTransfer =
  client.v1().treasury().inboundTransfers().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const inboundTransfer = await stripe.treasury.inboundTransfers.create(
  {
    origin_payment_method: '{{PAYMENTMETHOD_ID}}',
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
    amount: 20000,
    currency: 'usd',
    description: 'Funds for repair',
    statement_descriptor: 'Invoice 12',
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TreasuryInboundTransferCreateParams{
  OriginPaymentMethod: stripe.String("{{PAYMENTMETHOD_ID}}"),
  FinancialAccount: stripe.String("{{TREASURYFINANCIALACCOUNT_ID}}"),
  Amount: stripe.Int64(20000),
  Currency: stripe.String(stripe.CurrencyUSD),
  Description: stripe.String("Funds for repair"),
  StatementDescriptor: stripe.String("Invoice 12"),
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TreasuryInboundTransfers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Treasury.InboundTransferCreateOptions
{
    OriginPaymentMethod = "{{PAYMENTMETHOD_ID}}",
    FinancialAccount = "{{TREASURYFINANCIALACCOUNT_ID}}",
    Amount = 20000,
    Currency = "usd",
    Description = "Funds for repair",
    StatementDescriptor = "Invoice 12",
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.InboundTransfers;
Stripe.Treasury.InboundTransfer inboundTransfer = service.Create(options, requestOptions);
```

If successful, the response provides the `InboundTransfer` object. The object includes a `hosted_regulatory_receipt_url` that provides access to details of the transaction for the account holder on the Homebox platform.

```json
{
    "id": "{{INBOUND_TRANSFER_ID}}",
    "object": "inbound_transfer",
    "amount": 20000,
    "created": 1648071297,
    "currency": "usd",
    "description": "Funds for repair",
    "financial_account": "{{FINANCIAL_ACCOUNT_ID}}",
    "hosted_regulatory_receipt_url": "https://payments.stripe.com/regulatory-receipt/{{IBT_URL}}",
    "linked_flows": null,
    "livemode": false,
    "metadata": {},
    "origin_payment_method": "{{PAYMENT_METHOD_ID}}",
    ...
    "statement_descriptor": "Invoice 12",
    "status": "processing",
    ...
}
```

## Using PaymentMethods with OutboundPayments

You can also use a `PaymentMethod` to send funds to an external bank account owned by a third party using `OutboundPayment` objects.

Homebox wants to send funds to one of its connected account’s vendors on a recurring basis to pay for supplies. To do so, the platform needs to first get the customer ID using the `Customers` endpoint.

```curl
curl https://api.stripe.com/v1/customers \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d description="Test Customer"
```

```cli
stripe customers create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --description="Test Customer"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

customer = client.v1.customers.create(
  {description: 'Test Customer'},
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
customer = client.v1.customers.create(
  {"description": "Test Customer"},
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$customer = $stripe->customers->create(
  ['description' => 'Test Customer'],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

CustomerCreateParams params =
  CustomerCreateParams.builder().setDescription("Test Customer").build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Customer customer = client.v1().customers().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const customer = await stripe.customers.create(
  {
    description: 'Test Customer',
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.CustomerCreateParams{Description: stripe.String("Test Customer")}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1Customers.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new CustomerCreateOptions { Description = "Test Customer" };
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Customers;
Customer customer = service.Create(options, requestOptions);
```

The response provides the customers for the associated connected account. Homebox identifies the correct customer and records their ID.

```json
{"id": "{{CUSTOMER_ID}}",
  "object": "customer",
  "address": null,
  "balance": 0,
  "created": 1642523420,
  "currency": null,
  "default_source": null,
  "delinquent": false,
  "description": "Test Customer",
  "discount": null,
  "email": null,
  "invoice_prefix": "96A50A92",
  "invoice_settings": {
    "custom_fields": null,
    "default_payment_method": null,
    "footer": null
  },
  "livemode": false,
  "metadata": {
  },
  "name": null,
  "next_invoice_sequence": 1,
  "phone": null,
  "preferred_locales": [

  ],
  "shipping": null,
  "tax_exempt": "none"
}

```

Homebox then creates a `SetupIntent` using the ID. Because `OutboundPayments` are used for outbound money flows to third parties, Homebox makes sure to set `flow_directions` to `outbound` and doesn’t use `attach_to_self`, instead specifying a `customer`.

```curl
curl https://api.stripe.com/v1/setup_intents \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d customer="{{CUSTOMER_ID}}" \
  -d "flow_directions[]"=outbound \
  -d "payment_method_types[]"=us_bank_account \
  -d "payment_method_data[type]"=us_bank_account \
  -d "payment_method_data[us_bank_account][routing_number]"=110000000 \
  -d "payment_method_data[us_bank_account][account_number]"=000123456789 \
  -d "payment_method_data[us_bank_account][account_holder_type]"=individual \
  -d "payment_method_data[billing_details][name]"="John doe" \
  -d confirm=true
```

```cli
stripe setup_intents create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --customer="{{CUSTOMER_ID}}" \
  -d "flow_directions[0]"=outbound \
  -d "payment_method_types[0]"=us_bank_account \
  -d "payment_method_data[type]"=us_bank_account \
  -d "payment_method_data[us_bank_account][routing_number]"=110000000 \
  -d "payment_method_data[us_bank_account][account_number]"=000123456789 \
  -d "payment_method_data[us_bank_account][account_holder_type]"=individual \
  -d "payment_method_data[billing_details][name]"="John doe" \
  --confirm=true
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

setup_intent = client.v1.setup_intents.create(
  {
    customer: '{{CUSTOMER_ID}}',
    flow_directions: ['outbound'],
    payment_method_types: ['us_bank_account'],
    payment_method_data: {
      type: 'us_bank_account',
      us_bank_account: {
        routing_number: '110000000',
        account_number: '000123456789',
        account_holder_type: 'individual',
      },
      billing_details: {name: 'John doe'},
    },
    confirm: true,
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
setup_intent = client.v1.setup_intents.create(
  {
    "customer": "{{CUSTOMER_ID}}",
    "flow_directions": ["outbound"],
    "payment_method_types": ["us_bank_account"],
    "payment_method_data": {
      "type": "us_bank_account",
      "us_bank_account": {
        "routing_number": "110000000",
        "account_number": "000123456789",
        "account_holder_type": "individual",
      },
      "billing_details": {"name": "John doe"},
    },
    "confirm": True,
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$setupIntent = $stripe->setupIntents->create(
  [
    'customer' => '{{CUSTOMER_ID}}',
    'flow_directions' => ['outbound'],
    'payment_method_types' => ['us_bank_account'],
    'payment_method_data' => [
      'type' => 'us_bank_account',
      'us_bank_account' => [
        'routing_number' => '110000000',
        'account_number' => '000123456789',
        'account_holder_type' => 'individual',
      ],
      'billing_details' => ['name' => 'John doe'],
    ],
    'confirm' => true,
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SetupIntentCreateParams params =
  SetupIntentCreateParams.builder()
    .setCustomer("{{CUSTOMER_ID}}")
    .addFlowDirection(SetupIntentCreateParams.FlowDirection.OUTBOUND)
    .addPaymentMethodType("us_bank_account")
    .setPaymentMethodData(
      SetupIntentCreateParams.PaymentMethodData.builder()
        .setType(SetupIntentCreateParams.PaymentMethodData.Type.US_BANK_ACCOUNT)
        .setUsBankAccount(
          SetupIntentCreateParams.PaymentMethodData.UsBankAccount.builder()
            .setRoutingNumber("110000000")
            .setAccountNumber("000123456789")
            .setAccountHolderType(
              SetupIntentCreateParams.PaymentMethodData.UsBankAccount.AccountHolderType.INDIVIDUAL
            )
            .build()
        )
        .setBillingDetails(
          SetupIntentCreateParams.PaymentMethodData.BillingDetails.builder()
            .setName("John doe")
            .build()
        )
        .build()
    )
    .setConfirm(true)
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

SetupIntent setupIntent = client.v1().setupIntents().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const setupIntent = await stripe.setupIntents.create(
  {
    customer: '{{CUSTOMER_ID}}',
    flow_directions: ['outbound'],
    payment_method_types: ['us_bank_account'],
    payment_method_data: {
      type: 'us_bank_account',
      us_bank_account: {
        routing_number: '110000000',
        account_number: '000123456789',
        account_holder_type: 'individual',
      },
      billing_details: {
        name: 'John doe',
      },
    },
    confirm: true,
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.SetupIntentCreateParams{
  Customer: stripe.String("{{CUSTOMER_ID}}"),
  FlowDirections: []*string{stripe.String(stripe.SetupIntentFlowDirectionOutbound)},
  PaymentMethodTypes: []*string{stripe.String("us_bank_account")},
  PaymentMethodData: &stripe.SetupIntentCreatePaymentMethodDataParams{
    Type: stripe.String("us_bank_account"),
    USBankAccount: &stripe.SetupIntentCreatePaymentMethodDataUSBankAccountParams{
      RoutingNumber: stripe.String("110000000"),
      AccountNumber: stripe.String("000123456789"),
      AccountHolderType: stripe.String("individual"),
    },
    BillingDetails: &stripe.SetupIntentCreatePaymentMethodDataBillingDetailsParams{
      Name: stripe.String("John doe"),
    },
  },
  Confirm: stripe.Bool(true),
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1SetupIntents.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new SetupIntentCreateOptions
{
    Customer = "{{CUSTOMER_ID}}",
    FlowDirections = new List<string> { "outbound" },
    PaymentMethodTypes = new List<string> { "us_bank_account" },
    PaymentMethodData = new SetupIntentPaymentMethodDataOptions
    {
        Type = "us_bank_account",
        UsBankAccount = new SetupIntentPaymentMethodDataUsBankAccountOptions
        {
            RoutingNumber = "110000000",
            AccountNumber = "000123456789",
            AccountHolderType = "individual",
        },
        BillingDetails = new SetupIntentPaymentMethodDataBillingDetailsOptions
        {
            Name = "John doe",
        },
    },
    Confirm = true,
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.SetupIntents;
SetupIntent setupIntent = service.Create(options, requestOptions);
```

A successful call returns the following object to indicate the `PaymentMethod` is successfully attached to the customer.

```json
{
  "id": "{{SETUP_INTENT_ID}}",
  "object": "setup_intent",
  "application": "{{APPLICATION_ID}}",
  "cancellation_reason": null,
  "client_secret": "{{SETUP_INTENT_SECRET}}",
  "created": 1642528487,
  "customer": "{{CUSTOMER_ID}}",
  "description": null,
  "flow_directions": [
    "outbound"
  ],
  "last_setup_error": null,
  "latest_attempt": null,
  "livemode": false,
  "mandate": null,
  "metadata": {
  },
  "next_action": null,
  "on_behalf_of": null,
  "payment_method": "{{PAYMENT_METHOD_ID}}",
  "payment_method_options": {
  },
  "payment_method_types": [
    "us_bank_account"
  ],
  "single_use_mandate": null,
  "status": "succeeded",
  "usage": "off_session"
}
```

## Creating an OutboundPayment without a PaymentMethod

`PaymentMethods` aren’t required when sending or using `OutboundPayments` if you don’t have a need to store the recipient’s bank account, as in the case of one-off payments.

Homebox needed to pay a vendor 50 USD to cover the cost of supplies. To pay the vendor, the platform calls `OutboundPayments` with the external bank account information.

```curl
curl https://api.stripe.com/v1/treasury/outbound_payments \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d "destination_payment_method_data[type]"=us_bank_account \
  -d "destination_payment_method_data[us_bank_account][routing_number]"=110000000 \
  -d "destination_payment_method_data[us_bank_account][account_number]"=000123456789 \
  -d "destination_payment_method_data[us_bank_account][account_holder_type]"=individual \
  -d "destination_payment_method_data[billing_details][name]"="John Doe" \
  -d financial_account="{{TREASURYFINANCIALACCOUNT_ID}}" \
  -d statement_descriptor=payment_1 \
  -d amount=5000 \
  -d currency=usd
```

```cli
stripe treasury outbound_payments create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  -d "destination_payment_method_data[type]"=us_bank_account \
  -d "destination_payment_method_data[us_bank_account][routing_number]"=110000000 \
  -d "destination_payment_method_data[us_bank_account][account_number]"=000123456789 \
  -d "destination_payment_method_data[us_bank_account][account_holder_type]"=individual \
  -d "destination_payment_method_data[billing_details][name]"="John Doe" \
  --financial-account="{{TREASURYFINANCIALACCOUNT_ID}}" \
  --statement-descriptor=payment_1 \
  --amount=5000 \
  --currency=usd
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

outbound_payment = client.v1.treasury.outbound_payments.create(
  {
    destination_payment_method_data: {
      type: 'us_bank_account',
      us_bank_account: {
        routing_number: '110000000',
        account_number: '000123456789',
        account_holder_type: 'individual',
      },
      billing_details: {name: 'John Doe'},
    },
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
    statement_descriptor: 'payment_1',
    amount: 5000,
    currency: 'usd',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
outbound_payment = client.v1.treasury.outbound_payments.create(
  {
    "destination_payment_method_data": {
      "type": "us_bank_account",
      "us_bank_account": {
        "routing_number": "110000000",
        "account_number": "000123456789",
        "account_holder_type": "individual",
      },
      "billing_details": {"name": "John Doe"},
    },
    "financial_account": "{{TREASURYFINANCIALACCOUNT_ID}}",
    "statement_descriptor": "payment_1",
    "amount": 5000,
    "currency": "usd",
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$outboundPayment = $stripe->treasury->outboundPayments->create(
  [
    'destination_payment_method_data' => [
      'type' => 'us_bank_account',
      'us_bank_account' => [
        'routing_number' => '110000000',
        'account_number' => '000123456789',
        'account_holder_type' => 'individual',
      ],
      'billing_details' => ['name' => 'John Doe'],
    ],
    'financial_account' => '{{TREASURYFINANCIALACCOUNT_ID}}',
    'statement_descriptor' => 'payment_1',
    'amount' => 5000,
    'currency' => 'usd',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

OutboundPaymentCreateParams params =
  OutboundPaymentCreateParams.builder()
    .setDestinationPaymentMethodData(
      OutboundPaymentCreateParams.DestinationPaymentMethodData.builder()
        .setType(
          OutboundPaymentCreateParams.DestinationPaymentMethodData.Type.US_BANK_ACCOUNT
        )
        .setUsBankAccount(
          OutboundPaymentCreateParams.DestinationPaymentMethodData.UsBankAccount.builder()
            .setRoutingNumber("110000000")
            .setAccountNumber("000123456789")
            .setAccountHolderType(
              OutboundPaymentCreateParams.DestinationPaymentMethodData.UsBankAccount.AccountHolderType.INDIVIDUAL
            )
            .build()
        )
        .setBillingDetails(
          OutboundPaymentCreateParams.DestinationPaymentMethodData.BillingDetails.builder()
            .setName("John Doe")
            .build()
        )
        .build()
    )
    .setFinancialAccount("{{TREASURYFINANCIALACCOUNT_ID}}")
    .setStatementDescriptor("payment_1")
    .setAmount(5000L)
    .setCurrency("usd")
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

OutboundPayment outboundPayment =
  client.v1().treasury().outboundPayments().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const outboundPayment = await stripe.treasury.outboundPayments.create(
  {
    destination_payment_method_data: {
      type: 'us_bank_account',
      us_bank_account: {
        routing_number: '110000000',
        account_number: '000123456789',
        account_holder_type: 'individual',
      },
      billing_details: {
        name: 'John Doe',
      },
    },
    financial_account: '{{TREASURYFINANCIALACCOUNT_ID}}',
    statement_descriptor: 'payment_1',
    amount: 5000,
    currency: 'usd',
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TreasuryOutboundPaymentCreateParams{
  DestinationPaymentMethodData: &stripe.TreasuryOutboundPaymentCreateDestinationPaymentMethodDataParams{
    Type: stripe.String("us_bank_account"),
    USBankAccount: &stripe.TreasuryOutboundPaymentCreateDestinationPaymentMethodDataUSBankAccountParams{
      RoutingNumber: stripe.String("110000000"),
      AccountNumber: stripe.String("000123456789"),
      AccountHolderType: stripe.String("individual"),
    },
    BillingDetails: &stripe.TreasuryOutboundPaymentCreateDestinationPaymentMethodDataBillingDetailsParams{
      Name: stripe.String("John Doe"),
    },
  },
  FinancialAccount: stripe.String("{{TREASURYFINANCIALACCOUNT_ID}}"),
  StatementDescriptor: stripe.String("payment_1"),
  Amount: stripe.Int64(5000),
  Currency: stripe.String(stripe.CurrencyUSD),
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TreasuryOutboundPayments.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Treasury.OutboundPaymentCreateOptions
{
    DestinationPaymentMethodData = new Stripe.Treasury.OutboundPaymentDestinationPaymentMethodDataOptions
    {
        Type = "us_bank_account",
        UsBankAccount = new Stripe.Treasury.OutboundPaymentDestinationPaymentMethodDataUsBankAccountOptions
        {
            RoutingNumber = "110000000",
            AccountNumber = "000123456789",
            AccountHolderType = "individual",
        },
        BillingDetails = new Stripe.Treasury.OutboundPaymentDestinationPaymentMethodDataBillingDetailsOptions
        {
            Name = "John Doe",
        },
    },
    FinancialAccount = "{{TREASURYFINANCIALACCOUNT_ID}}",
    StatementDescriptor = "payment_1",
    Amount = 5000,
    Currency = "usd",
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Treasury.OutboundPayments;
Stripe.Treasury.OutboundPayment outboundPayment = service.Create(options, requestOptions);
```

The successful call returns the following object:

```json
{
  "id": "{{OUTBOUNDPAYMENT_ID}}",
  "object": "treasury.outbound_payment",
  "amount": 5000,
  "cancelable": true,
  "created": 1643033124,
  "currency": "usd",
  "customer": null,
  "description": null,
  "destination_payment_method": null,
  "destination_payment_method_details": {
    "billing_details": {
      "address": {
        "city": null,
        "country": null,
        "line1": null,
        "line2": null,
        "postal_code": null,
        "state": null
      },
      "email": null,
      "name": "John Doe",
      "phone": null
    },
    "type": "us_bank_account",
    "us_bank_account": {
      "account_holder_type": "individual",
      "bank_name": "STRIPE TEST BANK",
      "fingerprint": "SzrMIps1gg91aVKG",
      "last4": "6789",
      "network": "ach",
      "routing_number": "110000000"
    }
  },
  "end_user_details": {
    "present": false,
    "ip_address": null
  },
  "expected_arrival_date": 1643068800,
  "financial_account": "{{FINANCIAL_ACCOUNT_ID}}",
  "hosted_regulatory_receipt_url": "https://payments.stripe.com/regulatory-receipt/bot_test_xxx",
  "livemode": false,
  "metadata": {
  },
  "statement_descriptor": "payment_1",
  "status": "processing",
  "status_transitions": {
    "canceled_at": null,
    "failed_at": null,
    "posted_at": null,
    "processing_at": 1643033124,
    "returned_at": null
  },
  "transaction": "{{TRANSACTION_ID}}"
}
```

## See also

- [Using Financial Accounts for platforms to set up financial accounts and cards](https://docs.stripe.com/financial-accounts/connect/examples/financial-accounts.md)
- [API reference](https://docs.stripe.com/api/treasury/financial_accounts.md)
