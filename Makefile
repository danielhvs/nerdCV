repl:
	clj -A:dev

sample:
	clj -M:runner resources/sample.edn resources/profile.jpg

sample-2:
	clj -M:runner resources/sample.edn
