const { convert } = require('json-schema-to-case-class');
const fs = require('fs');

const options = {
  topLevelCaseClassName: null,
  generateComments: true,
  generateValidations: true
}


function generateSchemas() {
  const path = "./lightning/doc/schemas/listinvoices.schema.json"
  const name = path.split("/").slice(-1).pop().trim().replace("schema.json", "")
  console.log("Building schema: ", name)
  const schema = JSON.parse(fs.readFileSync(path, 'utf-8'))

  convert(schema, {...options, topLevelCaseClassName: name})
  .then(r => {
    console.log(r)
  })
}

generateSchemas()

function test() {
  const waitAnyInvoiceSchema = {
    "$schema": "http://json-schema.org/draft-07/schema#",
    "type": "object",
    "additionalProperties": true,
    "required": [
      "label",
      "description",
      "payment_hash",
      "status",
      "expires_at"
    ],
    "properties": {
      "label": {
        "type": "string",
        "description": "unique label supplied at invoice creation"
      },
      "description": {
        "type": "string",
        "description": "description used in the invoice"
      },
      "payment_hash": {
        "type": "hex",
        "description": "the hash of the *payment_preimage* which will prove payment",
        "maxLength": 64,
        "minLength": 64
      },
      "status": {
        "type": "string",
        "enum": [
          "paid",
          "expired"
        ],
        "description": "Whether it's paid or expired"
      },
      "expires_at": {
        "type": "u64",
        "description": "UNIX timestamp of when it will become / became unpayable"
      },
      "msatoshi": {
        "deprecated": "true"
      },
      "amount_msat": {
        "type": "msat",
        "description": "the amount required to pay this invoice"
      },
      "bolt11": {
        "type": "string",
        "description": "the BOLT11 string (always present unless *bolt12* is)"
      },
      "bolt12": {
        "type": "string",
        "description": "the BOLT12 string (always present unless *bolt11* is)"
      }
    },
    "allOf": [
      {
        "if": {
          "properties": {
            "status": {
              "type": "string",
              "enum": [
                "paid"
              ]
            }
          }
        },
        "then": {
          "additionalProperties": false,
          "required": [
            "pay_index",
            "amount_received_msat",
            "paid_at",
            "payment_preimage"
          ],
          "properties": {
            "label": {},
            "description": {},
            "payment_hash": {},
            "status": {},
            "msatoshi": {},
            "amount_msat": {},
            "bolt11": {},
            "bolt12": {},
            "expires_at": {},
            "pay_index": {
              "type": "u64",
              "description": "Unique incrementing index for this payment"
            },
            "msatoshi_received": {
              "deprecated": true
            },
            "amount_received_msat": {
              "type": "msat",
              "description": "the amount actually received (could be slightly greater than *amount_msat*, since clients may overpay)"
            },
            "paid_at": {
              "type": "u64",
              "description": "UNIX timestamp of when it was paid"
            },
            "payment_preimage": {
              "type": "hex",
              "description": "proof of payment",
              "maxLength": 64,
              "minLength": 64
            }
          }
        },
        "else": {
          "additionalProperties": false,
          "properties": {
            "label": {},
            "description": {},
            "payment_hash": {},
            "status": {},
            "msatoshi": {},
            "amount_msat": {},
            "bolt11": {},
            "bolt12": {},
            "expires_at": {}
          }
        }
      }
    ]
  }



  const r = convert(waitAnyInvoiceSchema, {...options, topLevelCaseClassName: "WaitAnyInvoice"})

  r.then(result => {
    console.log(result)
  })
}
