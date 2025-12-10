# Verification flows

Apply a reusable configuration across your integration.

Flows provide a way to save and reuse the same configuration across all of your integration interfaces. You can use each flow to create verification sessions through the API, in the Dashboard, or through the flow’s static link. Flows support the management of different verification use cases and can help ensure consistency across your integration.

## Create flows

To set up your Identity integration, first create a flow to store your desired configuration:

1. Visit the [Verification flows](https://dashboard.stripe.com/identity/verification-sessions) page in your Dashboard.
1. Name the flow.
1. Configure the verification behavior you want.
1. Click **Save**.

### Manage flows

The [Verification flows](https://dashboard.stripe.com/identity/verification-flows) page displays all of your flows. You can create distinct flows for different use-cases, such as:

- Marketing campaigns
- High-value versus low-value transactions
- Known high-risk users versus trusted users
- Any other relevant use case

After you create a flow, you can visit the details page to:

- View details and edit the flow
- View a list of all the verifications created from the flow
- Activate or deactivate the flow’s static link

### Update flows

You can use flows to deploy a new configuration to production. For example, if you want to add selfie checks to your document verifications, you can edit the flow in the Dashboard to add selfie verification. Any future verifications you create with this flow automatically adopt the updated configuration, so make sure to only apply changes that you want to adopt for future verifications.

## Use flows to verify users

After you create a flow, you have two options for how to initiate an identity verification using it.

#### Integration choices for verifcation flows - API

To use flows in your API integration, copy the flow ID from the details page and pass it in the [verification_flow](https://docs.stripe.com/api/identity/verification_sessions/create.md#create_identity_verification_session-verification_flow) parameter when you create a verification session.

```curl
curl https://api.stripe.com/v1/identity/verification_sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d verification_flow="{{IDENTITYVERIFICATIONFLOW_ID}}"
```

```cli
stripe identity verification_sessions create  \
  --verification-flow="{{IDENTITYVERIFICATIONFLOW_ID}}"
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

verification_session = client.v1.identity.verification_sessions.create({
  verification_flow: '{{IDENTITYVERIFICATIONFLOW_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
verification_session = client.v1.identity.verification_sessions.create({
  "verification_flow": "{{IDENTITYVERIFICATIONFLOW_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$verificationSession = $stripe->identity->verificationSessions->create([
  'verification_flow' => '{{IDENTITYVERIFICATIONFLOW_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

VerificationSessionCreateParams params =
  VerificationSessionCreateParams.builder()
    .setVerificationFlow("{{IDENTITYVERIFICATIONFLOW_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
VerificationSession verificationSession =
  client.v1().identity().verificationSessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const verificationSession = await stripe.identity.verificationSessions.create({
  verification_flow: '{{IDENTITYVERIFICATIONFLOW_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.IdentityVerificationSessionCreateParams{
  VerificationFlow: stripe.String("{{IDENTITYVERIFICATIONFLOW_ID}}"),
}
result, err := sc.V1IdentityVerificationSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Identity.VerificationSessionCreateOptions
{
    VerificationFlow = "{{IDENTITYVERIFICATIONFLOW_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Identity.VerificationSessions;
Stripe.Identity.VerificationSession verificationSession = service.Create(options);
```

### Include user-specific details

As with any verification session that you create with the API, you can attach user-specific data with the [metadata](https://docs.stripe.com/api/identity/verification_sessions/object.md#identity_verification_session_object-metadata) and [provided_details](https://docs.stripe.com/api/identity/verification_sessions/object.md#identity_verification_session_object-provided_details) parameters. The [client_reference_id](https://docs.stripe.com/api/identity/verification_sessions/object.md#identity_verification_session_object-client_reference_id) parameter provides a reference to a user in your system that you can look up later.

For example, here’s how you can attach a user-specific phone number, email address, and `client_reference_id` to a verification session:

```curl
curl https://api.stripe.com/v1/identity/verification_sessions \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d verification_flow="{{IDENTITYVERIFICATIONFLOW_ID}}" \
  -d "provided_details[phone]"=5555551212 \
  --data-urlencode "provided_details[email]"="user@domain.com" \
  -d client_reference_id=reference-token
```

```cli
stripe identity verification_sessions create  \
  --verification-flow="{{IDENTITYVERIFICATIONFLOW_ID}}" \
  -d "provided_details[phone]"=5555551212 \
  -d "provided_details[email]"="user@domain.com" \
  --client-reference-id=reference-token
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

verification_session = client.v1.identity.verification_sessions.create({
  verification_flow: '{{IDENTITYVERIFICATIONFLOW_ID}}',
  provided_details: {
    phone: '5555551212',
    email: 'user@domain.com',
  },
  client_reference_id: 'reference-token',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
verification_session = client.v1.identity.verification_sessions.create({
  "verification_flow": "{{IDENTITYVERIFICATIONFLOW_ID}}",
  "provided_details": {"phone": "5555551212", "email": "user@domain.com"},
  "client_reference_id": "reference-token",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$verificationSession = $stripe->identity->verificationSessions->create([
  'verification_flow' => '{{IDENTITYVERIFICATIONFLOW_ID}}',
  'provided_details' => [
    'phone' => '5555551212',
    'email' => 'user@domain.com',
  ],
  'client_reference_id' => 'reference-token',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

VerificationSessionCreateParams params =
  VerificationSessionCreateParams.builder()
    .setVerificationFlow("{{IDENTITYVERIFICATIONFLOW_ID}}")
    .setProvidedDetails(
      VerificationSessionCreateParams.ProvidedDetails.builder()
        .setPhone("5555551212")
        .setEmail("user@domain.com")
        .build()
    )
    .setClientReferenceId("reference-token")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
VerificationSession verificationSession =
  client.v1().identity().verificationSessions().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const verificationSession = await stripe.identity.verificationSessions.create({
  verification_flow: '{{IDENTITYVERIFICATIONFLOW_ID}}',
  provided_details: {
    phone: '5555551212',
    email: 'user@domain.com',
  },
  client_reference_id: 'reference-token',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.IdentityVerificationSessionCreateParams{
  VerificationFlow: stripe.String("{{IDENTITYVERIFICATIONFLOW_ID}}"),
  ProvidedDetails: &stripe.IdentityVerificationSessionCreateProvidedDetailsParams{
    Phone: stripe.String("5555551212"),
    Email: stripe.String("user@domain.com"),
  },
  ClientReferenceID: stripe.String("reference-token"),
}
result, err := sc.V1IdentityVerificationSessions.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Identity.VerificationSessionCreateOptions
{
    VerificationFlow = "{{IDENTITYVERIFICATIONFLOW_ID}}",
    ProvidedDetails = new Stripe.Identity.VerificationSessionProvidedDetailsOptions
    {
        Phone = "5555551212",
        Email = "user@domain.com",
    },
    ClientReferenceId = "reference-token",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Identity.VerificationSessions;
Stripe.Identity.VerificationSession verificationSession = service.Create(options);
```

#### Integration choices for verifcation flows - No-code

### Static link

You can use a flow’s static link to verify any number of your users.

Embed these static links in email templates, include them in chat support scripts, or use them anywhere that you connect your users with Stripe’s identity verification. A static link for a verification flow remains active indefinitely until you deactivate it in the Dashboard. A flow is usable in the API even when its static link is inactive.

Users provide their email address, then proceed through a new Verification Session that Stripe automatically creates for them. The identity verification UI from a no-code flow is identical to one you create directly through the API. You can view verification results in the Dashboard. Stripe sends webhooks to notify you of updates to the Verification Session.

### Query parameters

Static links support the following query parameters:

- `client_reference_id`: A reference that Stripe stores in the [client_reference_id](https://docs.stripe.com/api/identity/verification_sessions/object.md#identity_verification_session_object-client_reference_id) field of the Verification Session.
- `prefilled_email`: Stripe pre-fills this value when prompting users for an email address.

> You must URL-encode query parameters that contain special characters like `+`.

[Search the Dashboard](https://docs.stripe.com/dashboard/search.md) to find the Verification Sessions that you create with a static link from a user-provided email address or a `client_reference_id`. For example, if you need to verify a user with the `user_12345` ID in your system, add `?client_reference_id=user_12345` to the end of the static link. Stripe saves this ID to the [client_reference_id](https://docs.stripe.com/api/identity/verification_sessions/object.md#identity_verification_session_object-client_reference_id) field of the Verification Session, which you can find later in the Dashboard.
