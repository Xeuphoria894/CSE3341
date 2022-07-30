#!/usr/bin/env scheme-srfi-7

(program
  (requires srfi-34 srfi-78)
  (files "myfns.ss")
  (code

(define myinterpreter-with-exception-handling
  (lambda (progs)
    (call-with-current-continuation
      (lambda (k)
        (with-exception-handler
          (lambda (x)
            (display x)
            (newline)
            (k "Error")
			(newline))
          (lambda () (myinterpreter progs)))))))

(define (main args)
  (check-set-mode! 'report-failed)
  (display "Grading myfns.ss")
  (newline)

	(check (myinterpreter-with-exception-handling
           '(planProg 10))
         => 10)
	
	(check (myinterpreter-with-exception-handling
           '(planProg (planAdd 1 2)))
         => 3)
	
	(check (myinterpreter-with-exception-handling
           '(planProg (planMul 2 5)))
         => 10)
		 
	(check (myinterpreter-with-exception-handling
           '(planProg (planSub 0 10)))
         => -10)
		 
	(check (myinterpreter-with-exception-handling
           '(planProg (planIf 0 5 10)))
         => 10)
		 
	(check (myinterpreter-with-exception-handling
           '(planProg (planLet x 10 (planAdd x x))))
         => 20)
		 
	(check (myinterpreter-with-exception-handling
           '(planProg (planAdd 10 (planLet x 5 (planMul x x)))))
         => 35)
		 
	(check (myinterpreter-with-exception-handling
           '(planProg (planLet x (planSub 0 (planLet x 10 x)) (planAdd x (planLet x 1 (planAdd x x))))))
         => -8)
		 
	(check (myinterpreter-with-exception-handling
           '(planProg (planLet x (planSub 0 (planAdd 10 11)) (planLet y x (planMul x y)))))
         => 441)
		 
	(check (myinterpreter-with-exception-handling
           '(planProg (planIf (planAdd 0 1) (planLet x 10 x) (planLet x 15 x))))
         => 10)
		 
	(check (myinterpreter-with-exception-handling
           '(planProg (planLet a (planFunction b (planAdd b b)) (a 5))))
         => 10)
		 
	(check (myinterpreter-with-exception-handling
           '(planProg (planLet a (planFunction b (planAdd b b)) (planLet a 1 (planMul a a)))))
         => '1)
		 
	(check (myinterpreter-with-exception-handling
           '(planProg (planLet f (planFunction x (planAdd 10 x)) (f 5))))
         => '15)
		 
	(check (myinterpreter-with-exception-handling
           '(planProg (planLet f (planFunction x (planAdd 10 x)) (f (f 5)))))
         => '25)

  (display "Result:")
  (check-report))))