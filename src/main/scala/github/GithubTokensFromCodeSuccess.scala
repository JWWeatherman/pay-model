package github

import com.mathbot.pay.SecureIdentifier

case class GithubTokensFromCodeSuccess(sessionId: SecureIdentifier, tokens: GithubTokens)
