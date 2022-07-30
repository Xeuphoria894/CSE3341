; CSE3341 Lab6 (Version 2)
;Peter Luo, luo.956

;; This is Version 2; I visited Nick's Office
;; Hour on Sunday and he said he would grade both
;; versions (previous Version1 submitted on Friday and
;; this one) and take the highest as the final grade.

; C8 and C9 is failed, and the code is not taking
; care of planLet x (function ... ...) (expr);
;This is improved from last version since it works 
;with planFunction; however in the office hours
;Nick was not able to run the grading scheme tests
;so unsure with future 6 more test cases, however,
;it seems to work except for planLet x (function ... ...) (expr)

; To be honest I really think this is the
;hardest project. I'm still not sure about
;how to bind correctly and pass the function itself
;as a parameter to the PlanLet. Other functions
;are easy and seems to work.

;The planLet and planFunction still destroys my life.
;Still unsure how to figure out planFunction with
;planLet.

(define (myinterpreter prog)
        (evalExpr (car (cdr prog)) '() 'id_1)
)

;; The main eval function
(define (evalExpr expr binding_list oldID)
        (cond
                ( (integer? expr) expr)
         
                ( (symbol? expr) (getID expr  binding_list))
         
                ( (equal? (car expr) 'planIf)
                (evalPlanIf (cadr expr) (caddr expr) (cadddr expr) binding_list oldID))
         
                ( (equal? (car expr) 'planAdd)(evalPlanAdd (cadr expr) (caddr expr)  binding_list oldID))
         
                ( (equal? (car expr) 'planMul)
                (evalPlanMul (cadr expr) (caddr expr)  binding_list oldID))
         
                ( (equal? (car expr) 'planSub) 
                (evalPlanSub (cadr expr) (caddr expr)  binding_list oldID))
         
                ( (equal? (car expr) 'planLet)
                (evalPlanLet (cadr expr) (caddr expr) (cadddr expr) binding_list oldID) )
         
                ( (equal? (car expr) 'planFunction) (makePair binding_list oldID expr oldID))
         
                ( (pair? expr) (funcCall (car expr) (cadr expr) binding_list oldID))
         
                ;( (equal? (car expr) 'planFunction)
                ;(evalPlanFunction expr binding_list ;oldID) )
         )
)

;; planIf
(define (evalPlanIf expr1 expr2 expr3 binding_list oldID)
    (if  (> (evalExpr expr1 binding_list oldID) 0)
         
         (evalExpr expr2 binding_list oldID)
         
         (evalExpr expr3 binding_list oldID)
         
     )
  
)

;; planAdd
(define (evalPlanAdd expr1 expr2 binding_list oldID)
  
    (+ (evalExpr expr1 binding_list oldID) (evalExpr expr2 binding_list oldID) )
  
)

;; planMul
(define (evalPlanMul expr1 expr2 binding_list oldID)
  
    (* (evalExpr expr1 binding_list oldID) (evalExpr expr2 binding_list oldID) )
  
  )

;; planSub
(define (evalPlanSub expr1 expr2 binding_list oldID)
    
  (- (evalExpr expr1 binding_list oldID) (evalExpr expr2 binding_list oldID) )
  
)

;; getID for symbol
(define (getID ID binding_list)
    
    (cond
     
        ((equal? ID (caar binding_list)) (cdar binding_list))
        (else (getID ID (cdr binding_list)))
     
     )
  
)

;; planLet
(define (evalPlanLet id expr1 expr2 binding_list oldID)
      
    (if  (list?  expr1)
         
         (evalExpr expr2 (makePair binding_list id expr1 oldID)  id)
         
         (evalExpr expr2 (makePair binding_list id (evalExpr expr1 binding_list id) oldID ) id )
    )
)

(define (evalLet binding_list ID expr1 expr2)
  
    (evalExpr expr2 (cons (cons ID (evalExpr expr1 binding_list)) binding_list ) )
  )

;; make pairs, return the new binding_list
(define (makePair old_list ID value oldID)
  ;check value if it's a list
  ;check value if it's a function, if it is, then 
  ;if value is a function but not a function then
  
    ;(cons (cons ID value) old_list)
  
  (cond 
	((not (pair? value)) (cons (cons ID value) old_list))
	((equal? 'planFunction (car value)) (cons (cons ID value) old_list))
	(else (cons (cons ID (evalExpr value old_list oldID) ) old_list))
   )
)

;planFunction
(define (evalPlanFunction id expr1 expr2 binding_list oldID) 
  
    (if ( (car expr1) 'planFunction)
        
        (evalExpr expr2 (makePair binding_list id expr1 oldID))
        
        (evalExpr expr2 (makePair binding_list id (evalExpr expr1 binding_list) oldID))
    )
  
)

;funcCall
(define (funcCall id expr binding_list oldID)
  
   (evalExpr (caddr (getID id binding_list)) (makePair binding_list (cadr (getID id binding_list)) (evalExpr expr binding_list oldID) oldID) oldID)
  
)

;